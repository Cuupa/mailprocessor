package com.cuupa.mailprocessor.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ExternSemanticService {

    private final RestTemplate restTemplate;

    private final String semanticUrl;

    private final Gson gson = new Gson();

    public ExternSemanticService(final RestTemplate restTemplate, final String semanticUrl){
        this.restTemplate = restTemplate;
        this.semanticUrl = semanticUrl;
    }

    public List<SemanticResult> getSemanticResult(final String plainText) {
        final ResponseEntity<String>
                responseEntity =
                restTemplate.postForEntity(semanticUrl, plainText, String.class);
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            Type listType = new TypeToken<ArrayList<SemanticResult>>() {
            }.getType();
            return gson.fromJson(responseEntity.getBody(), listType);
        } else {
            return new ArrayList<>();
        }
    }
}
