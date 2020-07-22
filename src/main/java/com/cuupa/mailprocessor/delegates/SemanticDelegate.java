package com.cuupa.mailprocessor.delegates;

import com.cuupa.mailprocessor.process.ProcessInstanceHandler;
import com.cuupa.mailprocessor.services.ExternSemanticService;
import com.cuupa.mailprocessor.services.Metadata;
import com.cuupa.mailprocessor.services.SemanticResult;
import org.apache.commons.collections4.CollectionUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SemanticDelegate implements JavaDelegate {

    private final ExternSemanticService externSemanticService;

    public SemanticDelegate(final ExternSemanticService externSemanticService) {
        this.externSemanticService = externSemanticService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        ProcessInstanceHandler handler = new ProcessInstanceHandler(delegateExecution);
        final List<SemanticResult>
                semanticResult =
                externSemanticService.getSemanticResult(String.join("", handler.getPlainText()));

        semanticResult.forEach(result -> {
            final Collection<Metadata> metadata = CollectionUtils.emptyIfNull(result.getMetaData());
            handler.addTopic(result.getTopicName())
                   .setSender(result.getSender())
                   .addMetaData(metadata.stream()
                                        .filter(e -> !"sender".equals(e.getName()))
                                        .collect(Collectors.toList()));
        });
    }
}
