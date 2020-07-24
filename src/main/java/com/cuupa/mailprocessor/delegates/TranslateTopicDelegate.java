package com.cuupa.mailprocessor.delegates;

import com.cuupa.mailprocessor.process.ProcessInstanceHandler;
import com.cuupa.mailprocessor.services.TranslateService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.stream.Collectors;

public class TranslateTopicDelegate implements JavaDelegate {

    private final TranslateService translateService;

    public TranslateTopicDelegate(final TranslateService translateService) {
        this.translateService = translateService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        ProcessInstanceHandler handler = new ProcessInstanceHandler(delegateExecution);
        handler.setTopics(handler.getTopics()
                                 .stream()
                                 .map(e -> translateService.translate(e, handler.getLocale()))
                                 .collect(Collectors.toList()));
    }
}
