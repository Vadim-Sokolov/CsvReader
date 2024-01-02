package com.esg.csvreader.ui;

import com.esg.csvreader.CommandProcessor;
import com.esg.csvreader.apiservice.ApiServiceException;
import com.esg.csvreader.reader.CsvReaderException;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConsoleServiceTest {

    private final BufferedReader mockReader = mock(BufferedReader.class);
    private final CommandProcessor mockProcessor = mock(CommandProcessor.class);
    private final ConsoleService listener = new ConsoleService(mockProcessor, mockReader);

    @Test
    void forReadFileCommand_shouldCallReadFileMethod() throws IOException, UserInterfaceException, CsvReaderException {
        // GIVEN
        when(mockReader.readLine()).thenReturn("Read file").thenReturn("Exit");

        // WHEN
        listener.listenToConsole();

        // THEN
        verify(mockProcessor, times(1)).readFile();
    }

    @Test
    void forPostFileCommand_shouldCallPostMethod() throws IOException, UserInterfaceException, CsvReaderException {
        // GIVEN
        when(mockReader.readLine()).thenReturn("Post file").thenReturn("Exit");

        // WHEN
        listener.listenToConsole();

        // THEN
        verify(mockProcessor, times(1)).postContentToRemote();
    }

    @Test
    void forGetCustomerCommand_shouldCallReadLine() throws IOException, UserInterfaceException, ApiServiceException {
        // GIVEN
        when(mockReader.readLine()).thenReturn("Get customer").thenReturn("33").thenReturn("Exit");

        // WHEN
        listener.listenToConsole();

        // THEN
        verify(mockProcessor, times(1)).getCustomerByReferenceNumber(33);
    }

    @Test
    void forExitCommand_shouldCloseReader_andSetKeepRunningToFalse() throws IOException, UserInterfaceException {
        // GIVEN
        when(mockReader.readLine()).thenReturn("Exit");

        // WHEN
        listener.listenToConsole();

        // THEN
        verify(mockReader, times(1)).close();
        assertFalse(listener.shouldKeepRunning());
    }
}