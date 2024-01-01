package com.esg.csvreader;

import com.esg.csvreader.apiservice.ApiService;
import com.esg.csvreader.apiservice.ApiServiceException;
import com.esg.csvreader.dto.CustomerDto;
import com.esg.csvreader.reader.CsvReader;
import com.esg.csvreader.reader.CsvReaderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CommandProcessor {

    private final CsvReader csvReader;
    private final ApiService apiService;

    public CommandProcessor(CsvReader csvReader, ApiService apiService) {
        this.csvReader = csvReader;
        this.apiService = apiService;
    }

    public List<CustomerDto> readFile() throws CsvReaderException {
        log.debug("Calling CsvReader readCsvFile");
        return csvReader.readCsvFile();
    }

    public List<CustomerDto> postContentToRemote() throws CsvReaderException {
        log.debug("Calling ApiService POST");
        return apiService.postCustomersToRemote(csvReader.readCsvFile());
    }

    public void getCustomerByReference(Integer referenceNumber) throws ApiServiceException {
        log.debug("Calling ApiService GET");
        apiService.getCustomerInformationByReferenceNumber(referenceNumber);
    }
}
