package com.esg.csvreader.reader;

import com.esg.csvreader.CommandProcessor;
import com.esg.csvreader.PropertyLoader;
import com.esg.csvreader.dto.CustomerDto;
import com.opencsv.CSVReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CsvReader {

    private static final Logger logger = LogManager.getLogger(CommandProcessor.class);
    private static final String CSV_FILE_PATH = "csvFilePath";

    public List<CustomerDto> readCsvFile() throws CsvReaderException {

        var customerDtos = new ArrayList<CustomerDto>();
        try (Reader reader = new FileReader(PropertyLoader.PROPERTIES.getProperty(CSV_FILE_PATH), StandardCharsets.UTF_8);
             CSVReader csvReader = new CSVReader(reader)) {

            logger.debug("Reading csv file");
            csvReader.readAll().stream()
                    .skip(1)
                    .map(Arrays::asList)
                    .forEach(row -> {
                        try {
                            customerDtos.add(convertRowToCustomerDto(row));
                        } catch (CsvReaderException e) {
                            throw new RuntimeException(e);
                        }
                    });

        } catch (Exception e) {
            logger.error("Could not read csv file: " + e.getMessage());
            throw new CsvReaderException(e.getMessage());
        }
        logger.debug("Csv file read and converted to CustomerDto");
        return customerDtos;
    }

    private CustomerDto convertRowToCustomerDto(List<String> row) throws CsvReaderException {
        try {
            if (row.size() < 8) {
                throw new CsvReaderException("Invalid number of elements in the row");
            }
            return CustomerDto.builder()
                    .customerRef(Integer.parseInt(row.get(0)))
                    .customerName(row.get(1))
                    .addressLine1(row.get(2))
                    .addressLine2(row.get(3))
                    .town(row.get(4))
                    .county(row.get(5))
                    .country(row.get(6))
                    .postcode(row.get(7))
                    .build();
        } catch (Exception e) {
            logger.error("Could not convert row to CustomerDto");
            throw new CsvReaderException("Could not convert row to CustomerDto " + e.getMessage());
        }
    }
}
