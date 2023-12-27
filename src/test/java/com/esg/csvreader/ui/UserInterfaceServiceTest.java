package com.esg.csvreader.ui;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserInterfaceServiceTest {

    private final BufferedReader mockReader = mock(BufferedReader.class);
    private final UserInterfaceService uiService = new UserInterfaceService(mockReader);

    @Test
    void whenExitCommandReceived_shouldCloseReader_andSetKeepRunningToFalse() throws IOException, UserInterfaceException {
        // GIVEN
        when(mockReader.readLine()).thenReturn("Exit");

        // WHEN
        uiService.listenToConsole();

        // THEN
        verify(mockReader, times(1)).close();
        assertFalse(uiService.isKeepRunning());
    }
}