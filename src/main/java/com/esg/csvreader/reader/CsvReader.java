package com.esg.csvreader.reader;

import com.esg.csvreader.PropertyLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvReader {

    private static final String CSV_FILE_PATH = "csvFilePath";

    public List<String> readCsvFile() throws CsvReaderException {

        var listOfJsonStrings = new ArrayList<String>();
        try (Reader reader = new FileReader(PropertyLoader.PROPERTIES.getProperty(CSV_FILE_PATH), StandardCharsets.UTF_8);
             CSVReader csvReader = new CSVReader(reader)) {

            csvReader.readAll().stream()
                    .skip(1)
                    .map(Arrays::asList)
                    .forEach(row -> {
                        try {
                            listOfJsonStrings.add(convertRowToJson(row));
                        } catch (CsvReaderException e) {
                            throw new RuntimeException(e);
                        }
                    });

        } catch (Exception e) {
            throw new CsvReaderException(e.getMessage());
        }
        return listOfJsonStrings;
    }

    private String convertRowToJson(List<String> row) throws CsvReaderException {
        var objectMapper = new ObjectMapper();

        try {
            return objectMapper.writeValueAsString(row);
        } catch (Exception e) {
            throw new CsvReaderException("Could not convert row to JSON " + e.getMessage());
        }
    }
}
