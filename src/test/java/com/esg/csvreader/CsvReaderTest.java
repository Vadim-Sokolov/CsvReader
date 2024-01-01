package com.esg.csvreader;

import com.esg.csvreader.dto.CustomerDto;
import com.esg.csvreader.reader.CsvReader;
import com.esg.csvreader.reader.CsvReaderException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CsvReaderTest {

    private final CsvReader reader = new CsvReader();

    @Test
    void shouldReadCsvFileCorrectly() throws CsvReaderException {
        // GIVEN
        var expectedSize = 5;
        var expectedCustomerDto = CustomerDto.builder()
                .customerRef(Integer.parseInt("234"))
                .customerName("Joe Bloggs")
                .addressLine1("3 Quebeck Street")
                .addressLine2("")
                .town("Lindon")
                .county("Shropshire")
                .country("UK")
                .postcode("RW1 2BK")
                .build();

        // WHEN
        var actual = reader.readCsvFile();

        // THEN
        assertEquals(expectedSize, actual.size());
        assertTrue(actual.contains(expectedCustomerDto));
    }
}