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
public class FindMessageByCompositeKeyCommand implements Command{
    private final MessagesRepository megRepository;
    private final UUID conversationId;

    public FindMessageByCompositeKeyCommand(MessagesRepository megRepository, UUID conversationId) {
        this.megRepository = megRepository;
        this.conversationId = conversationId;
    }

    @Override
    public ResponseEntity<?> execute() {
        try {
            List<Message> messages = megRepository.findByCompositeKey(conversationId);
            if (messages == null || messages.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No messages found for the given conversation ID");
            }
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching messages");
        }
    }
}
