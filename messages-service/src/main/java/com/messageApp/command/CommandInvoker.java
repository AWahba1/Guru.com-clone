package com.messageApp.command;

import org.springframework.http.ResponseEntity;

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

