package com.esg.csvreader.ui;

import com.esg.csvreader.CommandProcessor;
import com.esg.csvreader.apiservice.ApiService;
import com.esg.csvreader.apiservice.ApiServiceException;
import com.esg.csvreader.dto.CustomerDto;
import com.esg.csvreader.reader.CsvReader;
import com.esg.csvreader.reader.CsvReaderException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandProcessorTest {

    private final CsvReader mockReader = mock(CsvReader.class);
    private final ApiService mockApiService = mock(ApiService.class);
    private final CommandProcessor commandProcessor = new CommandProcessor(mockReader, mockApiService);

    @Test
    void readFile_shouldReadCsvFileCorrectly() throws CsvReaderException {
        // GIVEN
        var expected = List.of(new CustomerDto(), new CustomerDto());
        when(mockReader.readCsvFile()).thenReturn(expected);

        // WHEN
        var actual = commandProcessor.readFile();

        // THEN
        verify(mockReader, times(1)).readCsvFile();
        assertEquals(expected, actual);
    }

    @Test
    void testPostContentToRemote() throws CsvReaderException {
        // GIVEN
        var customerList = List.of(new CustomerDto(), new CustomerDto());
        when(mockReader.readCsvFile()).thenReturn(customerList);

        // WHEN
        commandProcessor.postContentToRemote();

        // THEN
        verify(mockReader, times(1)).readCsvFile();
        verify(mockApiService, times(1)).postCustomersToRemote(customerList);
    }

    @Test
    void testGetCustomerByReference() throws ApiServiceException {
        // GIVEN
        var referenceNumber = 123;

        // WHEN
        commandProcessor.getCustomerByReferenceNumber(referenceNumber);

        // THEN
        verify(mockApiService, times(1)).getCustomerByReferenceNumber(referenceNumber);
    }
}