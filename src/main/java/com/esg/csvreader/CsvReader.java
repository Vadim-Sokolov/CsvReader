package com.esg.csvreader;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class CsvReader {

    public void readCsvFile() throws CsvReaderException, IOException {

        Reader reader = null;
        CSVReader csvReader = null;
        try {
            reader = new FileReader(PropertyLoader.PROPERTIES.getProperty("csvFilePath"), StandardCharsets.UTF_8);
            csvReader = new CSVReader(reader);

            csvReader.readAll().stream()
                    .map(Arrays::asList)
                    .forEach(row -> {
                        row.forEach(value -> System.out.print(value + "\t"));
                        System.out.println();
                    });
        } catch (IOException | CsvException e) {
            throw new CsvReaderException(e.getMessage());
        } finally {
            if (csvReader != null) {
                csvReader.close();
                reader.close();
            }
        }
    }
}
