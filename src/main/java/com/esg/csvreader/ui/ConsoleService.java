package com.esg.csvreader.ui;

import com.esg.csvreader.CommandProcessor;
import com.esg.csvreader.apiservice.ApiServiceException;
import com.esg.csvreader.reader.CsvReaderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ConsoleService {

    private static final List<String> CONSOLE_COMMAND = ConsoleCommand.getAllCommandValues();
    private final CommandProcessor commandProcessor;
    private final BufferedReader reader;
    private boolean keepRunning = true;

    @Autowired
    public ConsoleService(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public ConsoleService(CommandProcessor commandProcessor, BufferedReader reader) {
        this.commandProcessor = commandProcessor;
        this.reader = reader;
    }

    public void listenToConsole() throws UserInterfaceException, IOException {
        try {
            while (keepRunning) {
                var userInput = reader.readLine();

                if (userInput == null || userInput.isEmpty()) {
                    continue;
                }
                log.debug("Processing user input: " + userInput);
                processUserInput(userInput);
            }
        } catch (Exception e) {
            log.error("Could not process user input: " + e.getMessage());
            throw new UserInterfaceException("Could not process command: " + e.getMessage());
        } finally {
            reader.close();
        }
    }

    private void processUserInput(String userInput) {
        if (!CONSOLE_COMMAND.contains(userInput)) {
            commandNotRecognised(userInput);
        }
        if (userInput.equals(ConsoleCommand.READ_FILE.getCommandValue())) {
            readFile();
        }
        if (userInput.equals(ConsoleCommand.POST_FILE_CONTENTS.getCommandValue())) {
            postToRemote();
        }
        if (userInput.equals(ConsoleCommand.GET_CUSTOMER_INFO.getCommandValue())) {
            getCustomerByReference();
        }
        if (userInput.equals(ConsoleCommand.EXIT.getCommandValue())) {
            exitProgram();
        }
    }

    private void commandNotRecognised(String userInput) {
        log.debug("Command not recognised: {} Printing available commands", userInput);
        System.out.println("Command not recognised: " + userInput);
        printListOfCommands();
    }

    private void readFile() {
        log.debug("Calling CommandProcessor readFile");
        try {
            commandProcessor.readFile().forEach(System.out::println);
        } catch (CsvReaderException e) {
            log.error("Could not read csv file: " + e.getMessage());
            System.out.println("Could not read csv file.");
            printListOfCommands();
        }
    }

    private void postToRemote() {
        log.debug("Calling CommandProcessor POST");
        try {
            var savedCustomers = commandProcessor.postContentToRemote();
            if (!savedCustomers.isEmpty()) {
                log.info("Successfully saved customers remotely: " + savedCustomers.size());
            } else {
                log.warn("No customers were saved remotely.");
            }
        } catch (CsvReaderException e) {
            log.error("Could not read csv file: " + e.getMessage());
            System.out.println("Could not read csv file.");
            printListOfCommands();
        }
    }

    private void getCustomerByReference() {
        log.debug("Requesting user input for reference number");
        System.out.println("Please enter Customer Id:");
        var customerRefNumberOptional = getReferenceNumberFromConsole();
        if (customerRefNumberOptional.isPresent()) {
            log.debug("Calling CommandProcessor GET");
            try {
                System.out.println(commandProcessor.getCustomerByReferenceNumber(customerRefNumberOptional.get()));
            } catch (ApiServiceException e) {
                log.error("Could not get customer: " + e.getMessage());
                System.out.println("Could not get customer: " + e.getMessage());
                printListOfCommands();
            }
        }
    }

    private Optional<Integer> getReferenceNumberFromConsole() {
        var input = "Input not found";
        try {
            log.debug("Expecting user input for reference number");
            input = reader.readLine();
            log.debug("Attempting to parse user input to Integer");
            return Optional.of(Integer.parseInt(input));
        } catch (IOException | NumberFormatException e) {
            log.debug("Failed to get reference number for user input: " + input);
            log.debug("Exception type: " + e.getClass());
            log.debug("Exception message: " + e.getMessage());
            System.out.println("Reference number should be an integer.");
            System.out.println("You have entered: " + input);
            printListOfCommands();
        }
        return Optional.empty();
    }

    private void exitProgram() {
        log.debug("Exit command received. Exiting program.");
        System.out.println("Exiting program");
        keepRunning = false;
    }

    private void printListOfCommands() {
        System.out.println("Here are your options: ");
        CONSOLE_COMMAND.forEach(System.out::println);
    }

    public boolean shouldKeepRunning() {
        return keepRunning;
    }
 }
