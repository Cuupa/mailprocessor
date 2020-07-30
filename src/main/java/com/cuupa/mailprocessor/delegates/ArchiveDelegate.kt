package com.cuupa.mailprocessor.delegates

import com.cuupa.mailprocessor.MailprocessorConfiguration
import com.cuupa.mailprocessor.process.ProcessInstanceHandler
import com.cuupa.mailprocessor.services.TranslateService
import com.cuupa.mailprocessor.services.archive.FileProtocolFactory
import com.cuupa.mailprocessor.utli.StringConverter
import org.apache.juli.logging.LogFactory
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import java.nio.charset.StandardCharsets
import java.util.*

class ArchiveDelegate(private val mailprocessorConfiguration: MailprocessorConfiguration,
                      private val translateService: TranslateService) : JavaDelegate {

    override fun execute(delegateExecution: DelegateExecution) {
        val handler = ProcessInstanceHandler(delegateExecution)
        val configurationForUser = mailprocessorConfiguration.getConfigurationForUser(handler.username)

        FileProtocolFactory.getForPath(configurationForUser.archiveProperties.path).use { fileProtocol ->
            fileProtocol!!.init(configurationForUser.archiveProperties.username,
                                configurationForUser.archiveProperties.password)

            val topicNameFolder = getTopicNameFolder(handler.topics, configurationForUser.locale)
            val path = fileProtocol.createDirectories(configurationForUser.archiveProperties.path,
                                                      handler.pathToSave!! + topicNameFolder)

            val filename = handler.topics.joinToString("_", "[", "]_") + handler.fileName?.replace(" ", "_")

            val encodedFilename = stringConverter.convertTo(filename, StandardCharsets.UTF_8)
            handler.archived = !fileProtocol.exists(path, encodedFilename) && fileProtocol.save(path,
                                                                                                encodedFilename,
                                                                                                handler.fileContent)
            if (handler.archived) {
                handler.archivedFilename = encodedFilename
            }
        }
    }

    private fun getTopicNameFolder(topics: List<String>, locale: Locale): String {
        return if (topics.size > 1) {
            translateService.translate("SEVERAL_TOPICS", locale)
        } else {
            topics.first()
        }
    }

    companion object {
        private val LOG = LogFactory.getLog(ArchiveDelegate::class.java)
        private val stringConverter = StringConverter()
    }
}