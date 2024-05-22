package com.messageApp.command;

import com.messageApp.Controller.MessageService;
import com.messageApp.DTO.MessageInputDTO;
import com.messageApp.DTO.UpdateDTO;
import org.springframework.http.ResponseEntity;

public class UpdateMessageCommand implements Command {
    private final MessageService messageService;
    private final UpdateDTO dto;
    private ResponseEntity<?> responseEntity;

    public UpdateMessageCommand(MessageService messageService, UpdateDTO dto) {
        this.messageService = messageService;
        this.dto = dto;
    }

    @Override
    public void execute() {
        responseEntity = messageService.updateMessage(dto);
    }

    @Override
    public ResponseEntity<?> getResponse() {
        return responseEntity;
    }

}