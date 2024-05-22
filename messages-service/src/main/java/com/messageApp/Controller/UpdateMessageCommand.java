package com.messageApp.Controller;

import com.messageApp.Models.*;
import com.messageApp.DTO.*;
import com.messageApp.DTO.See_conversationsDTO;
import com.messageApp.DTO.UpdateDTO;
import com.messageApp.Models.Message;
import com.messageApp.Models.See_conversations;
import com.messageApp.Models.messagePrimaryKey;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
public class UpdateMessageCommand implements Command{
    private final MessagesRepository megRepository;
    private final UpdateDTO updateDTO;
    @Autowired
    public UpdateMessageCommand(MessagesRepository megRepository, UpdateDTO updateDTO) {
        this.megRepository = megRepository;
        this.updateDTO = updateDTO;
    }

    @Override
    public ResponseEntity<?> execute() {
        if (updateDTO.getMessage_text() == null && updateDTO.getMessage_file() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("message_text and message_file fields are both null");
        }
        if (updateDTO.getMessage_text().isEmpty() && updateDTO.getMessage_file().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("message_text and message_file fields are both empty");
        }
        try {
            if (updateDTO.getMessage_text() == null) {
                megRepository.updateMessageFile(updateDTO.getConversation_id(), updateDTO.getSent_at(), updateDTO.getMessage_id(), updateDTO.getMessage_file());
            } else if (updateDTO.getMessage_file() == null) {
                megRepository.updateMessageText(updateDTO.getConversation_id(), updateDTO.getSent_at(), updateDTO.getMessage_id(), updateDTO.getMessage_text());
            } else {
                megRepository.updateMessageBoth(updateDTO.getConversation_id(), updateDTO.getSent_at(), updateDTO.getMessage_id(), updateDTO.getMessage_text(), updateDTO.getMessage_file());
            }
            return ResponseEntity.ok("Message updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update message");
        }
    }
}
