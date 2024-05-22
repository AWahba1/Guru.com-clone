package com.messageApp.command;

import com.messageApp.Controller.MessageService;
import com.messageApp.DTO.UpdateDTO;
import com.messageApp.Models.*;
import org.springframework.http.ResponseEntity;

public class DeleteMessageCommand implements Command {
    private final MessageService messageService;
    private final messagePrimaryKey messagePrimaryKey;
    private ResponseEntity<?> responseEntity;

    public DeleteMessageCommand(MessageService messageService, messagePrimaryKey messagePrimaryKey) {
        this.messageService = messageService;
        this.messagePrimaryKey = messagePrimaryKey;
    }

    @Override
    public void execute() {
        responseEntity = messageService.deleteMessage(messagePrimaryKey);
    }

    @Override
    public ResponseEntity<?> getResponse() {
        return responseEntity;
    }

}