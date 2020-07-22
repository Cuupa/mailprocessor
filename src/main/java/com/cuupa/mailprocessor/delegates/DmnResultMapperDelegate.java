package com.cuupa.mailprocessor.delegates;

import com.cuupa.mailprocessor.process.ProcessInstanceHandler;
import com.cuupa.mailprocessor.services.Metadata;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DmnResultMapperDelegate implements JavaDelegate {

    private final Pattern regexPlaceholder = Pattern.compile("\\%[a-zA-Z]*\\%");

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        ProcessInstanceHandler handler = new ProcessInstanceHandler(delegateExecution);
        final String pathToSave = getPathToSave(handler.getDmnResult());
        final String replacedPath = replacePlaceholder(pathToSave, handler);
        handler.setPathToSave(replacedPath).setHasReminder(getReminder(handler.getDmnResult()));
    }

    private String replacePlaceholder(final String pathToSave, final ProcessInstanceHandler handler) {
        String newPath = pathToSave;
        if (StringUtils.isNotEmpty(handler.getSender())) {
            newPath = newPath.replace("%sender", handler.getSender());
        }

        final Matcher matcher = regexPlaceholder.matcher(pathToSave);
        while (matcher.find()) {
            final String varName = matcher.group();
            final List<Metadata>
                    collect =
                    handler.getMetadata()
                           .stream()
                           .filter(e -> e.getName().equals(varName.replace("%", "")))
                           .collect(Collectors.toList());
            newPath =
                    newPath.replace(varName, collect.stream().map(Metadata::getValue).collect(Collectors.joining("_")));
        }
        return newPath;

    }

    private String getPathToSave(final Map<String, Object> dmn_result) {
        return (String) dmn_result.get("path_to_save");
    }

    private Boolean getReminder(Map<String, Object> dmn_result) {
        return (Boolean) dmn_result.get("hasReminder");
    }
}
