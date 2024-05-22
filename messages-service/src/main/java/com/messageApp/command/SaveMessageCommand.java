package com.messageApp.command;

import com.messageApp.Controller.MessageService;
import com.messageApp.DTO.MessageInputDTO;
import org.springframework.http.ResponseEntity;

public class SaveMessageCommand implements Command {
    private final MessageService messageService;
    private final MessageInputDTO messageInputDTO;
    private ResponseEntity<?> responseEntity;

    public SaveMessageCommand(MessageService messageService, MessageInputDTO messageInputDTO) {
        this.messageService = messageService;
        this.messageInputDTO = messageInputDTO;
    }

    @Override
    public void execute() {
        responseEntity = messageService.saveMessage(messageInputDTO);
    }

    @Override
    public ResponseEntity<?> getResponse() {
        return responseEntity;
    }

}