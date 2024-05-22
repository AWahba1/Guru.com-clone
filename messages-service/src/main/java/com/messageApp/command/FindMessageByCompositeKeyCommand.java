package com.messageApp.command;

import com.messageApp.Controller.MessageService;
import com.messageApp.Models.Message;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public class FindMessageByCompositeKeyCommand implements Command {
    private final MessageService messageService;
    private final UUID conversationId;
    private ResponseEntity<?> responseEntity;

    public FindMessageByCompositeKeyCommand(MessageService messageService, UUID conversationId) {
        this.messageService = messageService;
        this.conversationId = conversationId;
    }

    @Override
    public void execute() {
        List<Message> messages = messageService.findMessageByCompositeKey(conversationId);
        responseEntity = ResponseEntity.ok(messages);
    }

    @Override
    public ResponseEntity<?> getResponse() {
        return responseEntity;
    }
}
