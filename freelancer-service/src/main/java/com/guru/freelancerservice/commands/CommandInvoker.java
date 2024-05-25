package com.guru.freelancerservice.commands;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CommandInvoker {
    private Command command;
    private ResponseEntity<?> response;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void executeCommand() {
        command.execute();
    }



    public ResponseEntity<?> getResponse() {
        return command.getResponse();
    }



}

