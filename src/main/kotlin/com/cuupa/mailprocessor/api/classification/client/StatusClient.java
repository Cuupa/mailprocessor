package com.cuupa.mailprocessor.api.classification.client;

import com.cuupa.mailprocessor.api.classification.model.Status;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author Simon Thiel (https://github.com/cuupa) (30.05.2021)
 */
public class StatusClient extends RestClient {

    @Value("url:localhost:8080")
    private String url;

    private final String path = "/status";

    public Status status() {
        ResponseEntity<Status> result = new RestTemplate().getForEntity(getUrlToCall(path), Status.class);
        if (result.getStatusCode().is2xxSuccessful()) {
            return result.getBody();
        }

        return null;
    }


}
