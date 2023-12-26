package com.esg.csvreader.service;

import com.esg.csvreader.PropertyLoader;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class ApiServiceImpl {

    private static final String POST_URL = PropertyLoader.PROPERTIES.getProperty("postEndPoint");
    private static final String GET_URL = PropertyLoader.PROPERTIES.getProperty("getEndPoint");
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

    public String getCustomerInformationByReferenceNumber(Integer refNumber) throws ApiServiceException {

        var apiUrlWithId = GET_URL + "/{" + refNumber + "}";

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrlWithId, HttpMethod.GET, null, String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            throw new ApiServiceException("Failed to retrieve record. HTTP Status: " + responseEntity.getStatusCode());
        }
    }
}
