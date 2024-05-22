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
public class DeleteMessageCommand implements Command{
    private final MessagesRepository megRepository;
    private final messagePrimaryKey mp;
    @Autowired
    public DeleteMessageCommand(MessagesRepository megRepository, messagePrimaryKey mp) {
        this.megRepository = megRepository;
        this.mp = mp;
    }

    @Override
    public ResponseEntity<?> execute() {
        try {
            megRepository.deleteByCompositeKey(mp.getConversation_id(), mp.getSent_at(), mp.getMessage_id());
            return ResponseEntity.ok("Message deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete message");
        }
    }
}
