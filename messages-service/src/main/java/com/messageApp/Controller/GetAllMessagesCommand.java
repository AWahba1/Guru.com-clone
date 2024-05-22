package com.messageApp.Controller;

import com.messageApp.Models.*;
import com.messageApp.DTO.*;
import com.messageApp.DTO.See_conversationsDTO;
import com.messageApp.DTO.UpdateDTO;
import com.messageApp.Models.Message;
import com.messageApp.Models.See_conversations;
import com.messageApp.Models.messagePrimaryKey;
import com.messageApp.messaging.NewMessageSentDTO;
import com.messageApp.messaging.ViewProfileMessageProducer;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
public class GetAllMessagesCommand implements Command{

    public  MessagesRepository megRepository;
@Autowired
    public GetAllMessagesCommand(MessagesRepository megRepository) {
        this.megRepository = megRepository;
    }
    public GetAllMessagesCommand(){

    }


    @Override
    @Cacheable(value = "messages", key = "'allMessages'")
    public ResponseEntity<?> execute() {
        try {
            List<Message> messages = megRepository.findAll();
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching messages");
        }
    }
}
