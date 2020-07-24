package com.cuupa.mailprocessor.delegates;

import com.cuupa.mailprocessor.process.ProcessInstanceHandler;
import com.cuupa.mailprocessor.services.archive.FileProtocol;
import com.cuupa.mailprocessor.services.archive.FileProtocolFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.Arrays;

public class ArchiveDelegate implements JavaDelegate {

    private static final Log LOG = LogFactory.getLog(ArchiveDelegate.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        ProcessInstanceHandler handler = new ProcessInstanceHandler(delegateExecution);

        try (FileProtocol fileProtocol = FileProtocolFactory.getForPath("address")) {
            final String path = createCollections(handler.getPathToSave(), fileProtocol);
            String filename = "[" + String.join("_", handler.getTopics()) + "]_" + handler.getFileName();
            filename = filename.replace(" ", "_");
            String url = path + filename;
            if(!fileProtocol.exists(url)) {
                fileProtocol.save(url, handler.getFileContent());
            }
        }
    }

    private String createCollections(String path, FileProtocol fileProtocol) {
        StringBuilder pathTemp = new StringBuilder("/");

        Arrays.stream(path.split("/")).filter(StringUtils::isNotBlank).forEach(e -> {
            pathTemp.append(e);
            pathTemp.append("/");
            String url = getUrlAsString(null, pathTemp.toString());

            if (!fileProtocol.exists(url)) {
                LOG.error("Creating collection " + url);
                fileProtocol.createDirectory(url);
            }
        });

        return pathTemp.toString();
    }

    private String getUrlAsString(Object properties, String path) {
        return "";
//        return new String(getUrl(properties, path).toString().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
    }

//    private URL getUrl(ArchiveProperties properties, String path) throws MalformedURLException, URISyntaxException {
//        return new URI(getScheme(properties.getAddress()),
//                       null,
//                       getHost(properties.getAddress()),
//                       getPort(properties.getAddress()),
//                       path,
//                       null,
//                       null).toURL();
//    }


}
