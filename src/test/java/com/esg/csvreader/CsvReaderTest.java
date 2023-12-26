package com.esg.csvreader;

import com.esg.csvreader.reader.CsvReader;
import com.esg.csvreader.reader.CsvReaderException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CsvReaderTest {

    private final CsvReader reader = new CsvReader();

    @Test
    void shouldReadCsvFileCorrectly() throws CsvReaderException, IOException {
        // GIVEN
        var expectedSize = 5;
        var expectedJsonString = "[\"234\",\"Joe Bloggs\",\"3 Quebeck Street\",\"\",\"Lindon\",\"Shropshire\",\"UK\",\"RW1 2BK\"]";

        // WHEN
        var actual = reader.readCsvFile();

        // THEN
        assertEquals(expectedSize, actual.size());
        assertTrue(actual.contains(expectedJsonString));
    }

    @Test
    void shouldSkipFirstRow() throws CsvReaderException, IOException {
        // GIVEN
        var expected = "[\"Customer Ref\",\"Customer Name\",\"Address Line 1\",\"Address Line 2\",\"Town\",\"County\",\"Country\",\"Postcode\"]";

        // WHEN
        var actual = reader.readCsvFile();

        // THEN
        assertFalse(actual.contains(expected));
    }
}