package com.cuupa.mailprocessor.services.input.email

import com.cuupa.mailprocessor.services.input.Attachment
import com.cuupa.mailprocessor.services.input.EMail
import com.cuupa.mailprocessor.userconfiguration.EmailProperties
import kotlinx.coroutines.*
import org.apache.commons.io.IOUtils
import org.apache.juli.logging.LogFactory
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class EmailService {

    fun loadMails(username: String, config: EmailProperties): List<EMail> {
        return when {
            isConfigValid(config) -> getMessagesAsync(config.labels!!, config, username)
            else -> listOf()
        }
    }

    private fun getMessagesSync(labels: List<String>, config: EmailProperties, username: String): List<EMail> {
        return labels.map { loadMailsForFolder(it, getStoreAndConnect(config), username) }.flatten()
    }

    private fun getMessagesAsync(labels: List<String>, config: EmailProperties, username: String): List<EMail> {
        var messages = listOf<EMail>()
        runBlocking {
            val listOfJobs = mutableListOf<Deferred<List<EMail>>>()
            val job = GlobalScope.launch {
                labels.forEach { label ->
                    val async = GlobalScope.async { loadMailsForFolder(label, getStoreAndConnect(config), username) }
                    listOfJobs.add(async)
                }
            }
            job.join()
            messages = listOfJobs.map { it.await() }.flatten()
        }
        return messages
    }

    private fun isConfigValid(
            config: EmailProperties) = (config.username.isNullOrEmpty() || config.password.isNullOrEmpty() || config.servername.isNullOrEmpty() || config.protocol.isNullOrEmpty())

    fun markMailAsRead(subject: String, label: String, receivedDate: LocalDateTime?, emailProperties: EmailProperties) {
        getStoreAndConnect(emailProperties).use {
            AutoClosableIMAPFolder(it.getStore().getFolder(label)).use { folder ->
                folder.open(Folder.READ_WRITE)
                markMailAsRead(subject, receivedDate, folder)
            }
        }
    }

    private fun markMailAsRead(subject: String, receivedDate: LocalDateTime?, folder: AutoClosableIMAPFolder) {
        val largestUid: Long = folder.uidNext - 1
        var offset: Long = 0
        while (offset < largestUid) {
            val messages = getMessages(folder, largestUid, offset)
            folder.fetch(messages, fetchProfile)
            for (i in messages.indices.reversed()) {
                val message = messages[i]
                if (isSubjectEquals(subject, message) && isReceivedDateEquals(receivedDate, message)) {
                    message.setFlag(Flags.Flag.SEEN, true)
                    return
                }
            }
            offset += chunkSize
        }
    }

    private fun isReceivedDateEquals(receivedDate: LocalDateTime?, message: Message): Boolean = LocalDateTime.ofInstant(
        message.receivedDate.toInstant(),
        ZoneId.systemDefault()) == receivedDate

    private fun isSubjectEquals(subject: String, message: Message): Boolean = message.subject == subject

    private fun loadMailsForFolder(folderName: String, store: AutoClosableStore, username: String): List<EMail> {
        val folder = AutoClosableIMAPFolder(store.getStore().getFolder(folderName)).open(Folder.READ_ONLY)
        return loadMessageFromFolder(folder, username)
    }

    private fun loadMessageFromFolder(folder: AutoClosableIMAPFolder, username: String): MutableList<EMail> {
        val largestUid = folder.uidNext - 1
        val messageList = mutableListOf<EMail>()
        var offset: Long = 0

        return if (folder.getUnreadMessageCount() == 0) {
            log.info("No mails for \"${folder.name()}\" found")
            mutableListOf()
        } else {

            log.info("${Thread.currentThread().name}: Fetching mails from \"${folder.name()}\"")
            for (i in 0..largestUid) {

                val messages = getMessages(folder, largestUid, offset)
                folder.fetch(messages, fetchProfile)

                val emails = messages.filter { !it.isSet(Flags.Flag.SEEN) }.map {
                    createEmail(it, getContent(it), username)
                }.filter { it.isValid }

                messageList.addAll(emails)
                offset += chunkSize
            }
            log.info("${Thread.currentThread().name}: Mails from \"${folder.name()}\" successfully fetched")
            messageList
        }
    }

    private fun getContent(it: Message): ByteArray {
        return try {
            val outputStream = ByteArrayOutputStream()
            it.writeTo(outputStream)
            outputStream.toByteArray()
        } catch (exception: IOException) {
            ByteArray(0)
        }
    }

    private fun createEmail(it: Message, content: ByteArray, username: String): EMail {
        val eMail = EMail()
        eMail.subject = it.subject
        eMail.label = it.folder.fullName
        eMail.receivedDate = LocalDateTime.ofInstant(it.receivedDate.toInstant(), ZoneId.systemDefault())
        eMail.content = content
        eMail.user = username
        eMail.from = (it.from[0] as InternetAddress).address
        eMail.filename = "${dateTimeFormatter.format(eMail.receivedDate)} - ${eMail.subject!!}.eml"
        eMail.attachments.addAll(getAttachments(eMail.content, username))
        return eMail
    }

    private fun getAttachments(content: ByteArray?, username: String): List<Attachment> {
        return when (val mimeMessage = MimeMessage(null, ByteArrayInputStream(content)).content){
            is Multipart -> getAttachments(mimeMessage, username)
            else -> listOf()
        }
    }

    private fun getAttachments(mimeMessage: Multipart, username: String): List<Attachment> {
        val attachments = mutableListOf<Attachment>()
        for (partIndex in 0 until mimeMessage.count) {
            val bodyPart = mimeMessage.getBodyPart(partIndex)
            if (Part.ATTACHMENT != bodyPart.disposition) {
                continue
            }
            val attachment = Attachment()
            attachment.filename = bodyPart.fileName
            attachment.content = IOUtils.toByteArray(bodyPart.inputStream)
            attachment.user = username
            attachments.add(attachment)
        }
        return attachments
    }

    private fun getMessages(folder: AutoClosableIMAPFolder, largestUid: Long, offset: Long): List<Message> {
        val start = Math.max(1, largestUid - offset - chunkSize + 1)
        val end = Math.max(1, largestUid - offset)
        return folder.getMessagesByUID(start, end)
    }

    private fun getStoreAndConnect(emailProperties: EmailProperties): AutoClosableStore {
        val store: Store = getStore(emailProperties)
        store.connect(emailProperties.servername, emailProperties.username, emailProperties.password)
        return AutoClosableStore(store)
    }

    private fun getStore(emailProperties: EmailProperties): Store {
        val properties = Properties()
        properties.setProperty("mail.store.protocol", emailProperties.protocol)
        properties.setProperty("mail.imaps.host", emailProperties.servername)
        properties.setProperty("mail.imaps.port", "${emailProperties.port}")
        properties.setProperty("mail.imaps.connectiontimeout", "5000")
        properties.setProperty("mail.imaps.timeout", "5000")
        val session = Session.getInstance(properties)
        return session.getStore(emailProperties.protocol)
    }

    init {
        fetchProfile.add(FetchProfile.Item.FLAGS)
        fetchProfile.add(FetchProfile.Item.ENVELOPE)
    }

    companion object {
        private val fetchProfile = FetchProfile()
        private const val chunkSize = 500
        private val log = LogFactory.getLog(EmailService::class.java)
        private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH_mm_ss")
    }
}
