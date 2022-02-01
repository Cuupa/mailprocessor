package com.cuupa.mailprocessor.api.classification.client;

import com.cuupa.mailprocessor.api.classification.model.ClassificationRequest;
import com.cuupa.mailprocessor.api.classification.model.ClassificationResult;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author Simon Thiel (https://github.com/cuupa) (30.05.2021)
 */
public class ClassificationClient extends RestClient {

    private final String path = "classification";

    public ClassificationClient(final String url){
        this.url = url;
    }

    public ClassificationClient() {}

    public ClassificationResult classify(ClassificationRequest request) {
        final HttpEntity<ClassificationRequest> entity = new HttpEntity<>(request, headers);

        final ResponseEntity<ClassificationResult> result = new RestTemplate().postForEntity(getUrlToCall(path), entity, ClassificationResult.class);
        if (result.getStatusCode().is2xxSuccessful()) {
            return result.getBody();
        }

        return null;
    }
}
