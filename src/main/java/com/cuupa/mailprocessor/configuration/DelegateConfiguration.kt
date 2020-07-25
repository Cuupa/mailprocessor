package com.cuupa.mailprocessor.configuration;

import com.cuupa.mailprocessor.delegates.DmnResultMapperDelegate;
import com.cuupa.mailprocessor.delegates.PlaintextDelegate;
import com.cuupa.mailprocessor.delegates.SemanticDelegate;
import com.cuupa.mailprocessor.services.semantic.ExternSemanticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DelegateConfiguration {

    @Autowired
    private ExternSemanticService externSemanticService;

    @Bean
    public PlaintextDelegate plaintextDelegate() {
        return new PlaintextDelegate();
    }

    @Bean
    public SemanticDelegate semanticDelegate() {
        return new SemanticDelegate(externSemanticService);
    }

    @Bean
    public DmnResultMapperDelegate dmnResultMapperDelegate() {
        return new DmnResultMapperDelegate();
    }
}
