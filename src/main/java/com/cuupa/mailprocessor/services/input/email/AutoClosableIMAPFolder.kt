package com.cuupa.mailprocessor.services.input.email

import com.sun.mail.imap.IMAPFolder
import javax.mail.FetchProfile
import javax.mail.Folder
import javax.mail.Message

class AutoClosableIMAPFolder(private val folder: Folder?) : AutoCloseable {

    val uidNext: Long
        get() {
            return if (folder is IMAPFolder) {
                folder.uidNext
            } else {
                0
            }
        }

    override fun close() {
        folder?.close()
    }

    fun open(state: Int) {
        folder?.open(state)
    }

    fun getMessagesByUID(start: Long, end: Long): Array<Message?> {
        return if (folder is IMAPFolder) {
            folder.getMessagesByUID(start, end)
        } else {
            arrayOfNulls(0)
        }
    }

    fun fetch(messages: Array<Message?>?, fetchProfile: FetchProfile) {
        folder?.fetch(messages, fetchProfile)
    }
}
