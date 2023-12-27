package com.esg.csvreader.ui;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ConsoleCommand {
    READ_FILE("Read file"),
    POST_FILE_CONTENTS("Post file"),
    GET_CUSTOMER_INFO("Get customer"),
    EXIT("Exit");

    private final String commandValue;

    ConsoleCommand(String commandValue) {
        this.commandValue = commandValue;
    }

    public String getCommandValue() {
        return commandValue;
    }

    public static List<String> getAllCommandValues() {
        return Arrays.stream(values())
                .map(ConsoleCommand::getCommandValue)
                .collect(Collectors.toList());
    }
}
