package com.cuupa.mailprocessor.delegates

import com.cuupa.mailprocessor.process.ProcessInstanceHandler
import com.cuupa.mailprocessor.services.archive.FileProtocol
import com.cuupa.mailprocessor.services.archive.FileProtocolFactory
import org.apache.juli.logging.LogFactory
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import java.util.*

class ArchiveDelegate : JavaDelegate {

    override fun execute(delegateExecution: DelegateExecution) {
        val handler = ProcessInstanceHandler(delegateExecution)
        FileProtocolFactory.getForPath("address").use { fileProtocol ->
            val path = createCollections(handler.pathToSave!!, fileProtocol!!)

            var filename = handler.topics.joinToString("_", "[", "]_") + handler.fileName
            filename = filename.replace(" ", "_")
            val url = path + filename
            if (!fileProtocol.exists(url)) {
                fileProtocol.save(url, handler.fileContent)
            }
        }
    }

    private fun createCollections(path: String, fileProtocol: FileProtocol): String {
        val pathTemp = StringBuilder("/")
        Arrays.stream(path.split("/".toRegex()).toTypedArray())
                .filter { cs: String? -> !cs.isNullOrBlank() }
                .forEach { e: String? ->
                    pathTemp.append(e)
                    pathTemp.append("/")
                    val url = getUrlAsString(null, pathTemp.toString())
                    if (!fileProtocol.exists(url)) {
                        LOG.error("Creating collection $url")
                        fileProtocol.createDirectory(url)
                    }
                }
        return pathTemp.toString()
    }

    private fun getUrlAsString(properties: Any?, path: String): String {
        return ""
        //        return new String(getUrl(properties, path).toString().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
    } //    private URL getUrl(ArchiveProperties properties, String path) throws MalformedURLException, URISyntaxException {

    //        return new URI(getScheme(properties.getAddress()),
    //                       null,
    //                       getHost(properties.getAddress()),
    //                       getPort(properties.getAddress()),
    //                       path,
    //                       null,
    //                       null).toURL();
    //    }
    companion object {
        private val LOG = LogFactory.getLog(ArchiveDelegate::class.java)
    }
}