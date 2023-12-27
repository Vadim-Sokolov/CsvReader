package com.esg.csvreader.ui;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserInterface implements CommandLineRunner {

    private final UserInterfaceService userInterfaceService;

    public UserInterface(UserInterfaceService userInterfaceService) {
        this.userInterfaceService = userInterfaceService;
    }

    @Override
    public void run(String... args) throws Exception {
        printGreeting();
        userInterfaceService.listenToConsole();
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
