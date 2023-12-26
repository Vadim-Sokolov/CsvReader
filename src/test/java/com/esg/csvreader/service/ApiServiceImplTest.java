package com.esg.csvreader.service;

import com.esg.csvreader.PropertyLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ApiServiceImplTest {

    private static final String POST_URL = PropertyLoader.PROPERTIES.getProperty("postEndPoint");
    private static final String GET_URL = PropertyLoader.PROPERTIES.getProperty("getEndPoint");
    private final RestTemplate restTemplate = mock(RestTemplate.class);
    private final ApiServiceImpl apiService = new ApiServiceImpl(restTemplate);

    @Test
    void shouldPostAllTheItemsOnTheList() {
        // GIVEN
        var contents = Arrays.asList("Record1", "Record2", "Record3");

        // WHEN
        apiService.postFileContentsToServerDatabase(contents);

        // THEN
        Mockito.verify(restTemplate, Mockito.times(contents.size()))
                .postForObject(eq(POST_URL), Mockito.any(HttpEntity.class), eq(String.class));

    }

    @Test
    void successCase_getCustomerInformationByReferenceNumber() throws ApiServiceException {
        // GIVEN
        var refNumber = 123;
        var expectedResponseBody = "Customer data";
        var successfulResponse = new ResponseEntity<>(expectedResponseBody, HttpStatus.OK);

        when(restTemplate.exchange(eq(GET_URL + "/{123}"), eq(HttpMethod.GET), isNull(), eq(String.class)))
                .thenReturn(successfulResponse);

        // WHEN
        var actual = apiService.getCustomerInformationByReferenceNumber(refNumber);

        // THEN
        assertEquals(expectedResponseBody, actual);
    }

    @Test
    void failureCase_getCustomerInformationByReferenceNumber() {
        // GIVEN
        var refNumber = 123;
        var expected = "Failed to retrieve record. HTTP Status: " + HttpStatus.INTERNAL_SERVER_ERROR;
        ResponseEntity<String> failedResponse = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        when(restTemplate.exchange(eq(GET_URL + "/{123}"), eq(HttpMethod.GET), isNull(), eq(String.class)))
                .thenReturn(failedResponse);

        // WHEN
        var thrown = Assertions.assertThrows(ApiServiceException.class,
                () -> apiService.getCustomerInformationByReferenceNumber(refNumber));

        // THEN
        assertEquals(expected, thrown.getMessage());
    }
}