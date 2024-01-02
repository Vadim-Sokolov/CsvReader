package com.esg.csvreader.apiservice;

import com.esg.csvreader.PropertyLoader;
import com.esg.csvreader.dto.CustomerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ApiService {

    private static final String POST_URL = PropertyLoader.PROPERTIES.getProperty("postEndPoint");
    private static final String GET_URL = PropertyLoader.PROPERTIES.getProperty("getEndPoint");
    private final RestTemplate restTemplate;

    public ApiService() {
        this.restTemplate = new RestTemplate();
    }

    public ApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<CustomerDto> postCustomersToRemote(List<CustomerDto> customers) {
        List<CustomerDto> savedCustomers = new ArrayList<>();

        log.debug("Processing customer list");
        customers.forEach(customer -> {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            log.debug("Creating new request entity");
            var requestEntity = new HttpEntity<>(customer, headers);

            try {
                log.debug("Posting to remote");
                var responseEntity = restTemplate.postForEntity(POST_URL, requestEntity, CustomerDto.class);
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    log.debug("Response successful");
                    savedCustomers.add(responseEntity.getBody());
                } else {
                    log.error("Failed to save customer: " + customer.getCustomerRef() + ". Response: " + responseEntity);
                }
            } catch (Exception e) {
                log.error("Error while processing customer: " + customer.getCustomerRef(), e);
            }
        });
        return savedCustomers;
    }

    public CustomerDto getCustomerByReferenceNumber(Integer refNumber) throws ApiServiceException {

        var apiUrlWithId = UriComponentsBuilder.fromUriString(GET_URL)
                .path("/{refNumber}")
                .buildAndExpand(refNumber)
                .toUriString();

        var responseEntity = restTemplate.exchange(
                apiUrlWithId, HttpMethod.GET, null, CustomerDto.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            throw new ApiServiceException("Failed to retrieve record. HTTP Status: " + responseEntity.getStatusCode());
        }
    }
}
