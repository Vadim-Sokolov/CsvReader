package com.esg.csvreader.ui;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class UserInterfaceService {

    private static final List<String> CONSOLE_COMMAND = ConsoleCommand.getAllCommandValues();
    private final BufferedReader reader;
    private boolean keepRunning = true;

    public UserInterfaceService() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public UserInterfaceService(BufferedReader reader) {
        this.reader = reader;
    }

    public void listenToConsole() throws UserInterfaceException, IOException {
        try {
            while (keepRunning) {
                var userInput = reader.readLine();

                if (userInput == null || userInput.isEmpty()) {
                    continue;
                }
                processUserInput(userInput);
            }
        } catch (Exception e) {
            throw new UserInterfaceException("Could not process command: " + e.getMessage());
        } finally {
            reader.close();
        }
    }

    private void processUserInput(String userInput) throws IOException {
        if (!CONSOLE_COMMAND.contains(userInput)) {
            System.out.println("Command not recognised. Here is the list of available commands:");
            CONSOLE_COMMAND.forEach(System.out::println);
        }
        if (userInput.equals(ConsoleCommand.READ_FILE.getCommandValue())) {
            System.out.println("Found command Read File");
        }
        if (userInput.equals(ConsoleCommand.POST_FILE_CONTENTS.getCommandValue())) {
            System.out.println("Found command Post File Contents");
        }
        if (userInput.equals(ConsoleCommand.GET_CUSTOMER_INFO.getCommandValue())) {
            System.out.println("Please enter Customer Id:");
            var customerId = reader.readLine();
            System.out.println("Retrieving customer information: " + customerId);
        }
        if (userInput.equals(ConsoleCommand.EXIT.getCommandValue())) {
            System.out.println("Exiting program");
            keepRunning = false;
        }
    }

    public boolean isKeepRunning() {
        return keepRunning;
    }
}
