package com.esg.csvreader.service;

import com.esg.csvreader.PropertyLoader;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.mockito.Mockito.mock;

class ApiServiceImplTest {

    private static final String POST_URL = PropertyLoader.PROPERTIES.getProperty("postEndPoint");
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
                .postForObject(Mockito.eq(POST_URL), Mockito.any(HttpEntity.class), Mockito.eq(String.class));

    }
}