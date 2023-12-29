package com.esg.csvreader;

import com.esg.csvreader.reader.CsvReaderException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyLoader {

    public static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = CsvReaderApplication.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new CsvReaderException("Sorry, unable to find application.properties");
            }
            PROPERTIES.load(input);
        } catch (IOException | CsvReaderException e) {
            throw new RuntimeException(e);
        }
    }
}
