package com.esg.csvreader.ui;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserInterface implements CommandLineRunner {

    private final ConsoleService consoleService;

    public UserInterface(ConsoleService consoleService) {
        this.consoleService = consoleService;
    }

    @Override
    public void run(String... args) throws Exception {
        printGreeting();
        consoleService.listenToConsole();
    }

    private void printGreeting() {
        System.out.println();
        System.out.println("*** CsvReaderApplication started! ***");
        System.out.println();
        System.out.println("Here is the list of available commands:");
        System.out.println("=======================================");
        ConsoleCommand.getAllCommandValues().forEach(System.out::println);
    }
}
