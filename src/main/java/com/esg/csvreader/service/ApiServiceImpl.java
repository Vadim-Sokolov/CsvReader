package com.esg.csvreader.service;

import com.esg.csvreader.PropertyLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class ApiServiceImpl {

    private static final String POST_URL = PropertyLoader.PROPERTIES.getProperty("postEndPoint");
    private final RestTemplate restTemplate;

    public ApiServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void postFileContentsToServerDatabase(List<String> contents) {
        contents.forEach(record -> {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            var requestEntity = new HttpEntity<>(record, headers);

            restTemplate.postForObject(POST_URL, requestEntity, String.class);
        });
    }
}
