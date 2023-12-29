package com.esg.csvreader;

import com.esg.csvreader.apiservice.ApiService;
import com.esg.csvreader.apiservice.ApiServiceException;
import com.esg.csvreader.reader.CsvReader;
import com.esg.csvreader.reader.CsvReaderException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandProcessor {

    private static final Logger logger = LogManager.getLogger(CommandProcessor.class);
    private final CsvReader csvReader;
    private final ApiService apiService;

    public CommandProcessor(CsvReader csvReader, ApiService apiService) {
        this.csvReader = csvReader;
        this.apiService = apiService;
    }

    public List<String> readFile() throws CsvReaderException {
        logger.debug("Calling CsvReader readCsvFile");
        return csvReader.readCsvFile();
    }

    public void postContentToRemote() throws CsvReaderException {
        logger.debug("Calling ApiService POST");
        apiService.postFileContentsToServerDatabase(csvReader.readCsvFile());
    }

    public void getCustomerByReference(Integer referenceNumber) throws ApiServiceException {
        logger.debug("Calling ApiService GET");
        apiService.getCustomerInformationByReferenceNumber(referenceNumber);
    }
}
