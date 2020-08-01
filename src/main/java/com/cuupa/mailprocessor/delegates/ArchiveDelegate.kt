package com.cuupa.mailprocessor.delegates

import com.cuupa.mailprocessor.MailprocessorConfiguration
import com.cuupa.mailprocessor.process.ErrorProperties
import com.cuupa.mailprocessor.process.ProcessInstanceHandler
import com.cuupa.mailprocessor.services.TranslateService
import com.cuupa.mailprocessor.services.files.FileFactory
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import java.util.*

class ArchiveDelegate(private val mailprocessorConfiguration: MailprocessorConfiguration,
                      private val translateService: TranslateService) : JavaDelegate {

    override fun execute(delegateExecution: DelegateExecution) {
        val handler = ProcessInstanceHandler(delegateExecution)
        val config = mailprocessorConfiguration.getConfigurationForUser(handler.username)
        val archiveProperties = config.archiveProperties
        FileFactory.getForPath(archiveProperties.path).use { file ->
            file.init(archiveProperties.username, archiveProperties.password)

            val path = file.createDirectories(archiveProperties.path,
                                              "${handler.pathToSave}${getTopicNameFolder(handler.topics,
                                                                                         config.locale)}")

            val filename = handler.topics.joinToString("_", "[", "]_") + handler.fileName
            val fileAlreadyExists = file.exists(path, filename)

            when {
                !fileAlreadyExists -> handler.archived = file.save(path, filename, handler.fileContent)
                else -> handleFileAlreadyExists(handler, path, filename)
            }
            when {
                handler.archived -> handler.archivedFilename = filename
                !handler.archived && !fileAlreadyExists -> handelArchiveError(handler, path, filename)
            }
        }
    }

    private fun handelArchiveError(handler: ProcessInstanceHandler, path: String, encodedFilename: String) {
        handler.addError(ErrorProperties.FILE_FAILED_TO_SAVE, "$path/$encodedFilename")
        handler.archived = false
    }

    private fun handleFileAlreadyExists(handler: ProcessInstanceHandler, path: String, encodedFilename: String) {
        handler.addError(ErrorProperties.FILE_ALREADY_EXISTS, "$path/$encodedFilename")
        handler.archived = false
    }

    private fun getTopicNameFolder(topics: List<String>, locale: Locale) = when (topics.size) {
        1 -> translateService.translate("SEVERAL_TOPICS", locale)
        else -> topics.first()
    }
}