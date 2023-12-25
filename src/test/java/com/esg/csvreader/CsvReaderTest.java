package com.esg.csvreader;

import org.junit.jupiter.api.Test;

import java.io.IOException;

class CsvReaderTest {

    private final CsvReader reader = new CsvReader();

    @Test
    void readCsvFile() throws CsvReaderException, IOException {
        reader.readCsvFile();
    }
}