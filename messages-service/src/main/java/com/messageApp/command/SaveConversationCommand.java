package com.messageApp.command;

import com.messageApp.Controller.See_conversationService;
import com.messageApp.DTO.See_conversationsDTO;
import org.springframework.http.ResponseEntity;

public class SaveConversationCommand implements Command {
    private final See_conversationService seeConversationService;
    private final See_conversationsDTO seeConversationsDTO;
    private ResponseEntity<?> responseEntity;

    public SaveConversationCommand(See_conversationService seeConversationService, See_conversationsDTO seeConversationsDTO) {
        this.seeConversationService = seeConversationService;
        this.seeConversationsDTO = seeConversationsDTO;
    }

    @Override
    public void execute() {
        responseEntity = seeConversationService.saveConversation(seeConversationsDTO);
    }

    @Override
    public ResponseEntity<?> getResponse() {
        return responseEntity;
    }
}
