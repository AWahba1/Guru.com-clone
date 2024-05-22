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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
public class SaveMessageCommand implements Command{
    private final MessagesRepository megRepository;
    private final See_conversationsRepository seeRepository;
    private final ViewProfileMessageProducer viewProfileMessageProducer;
    private final MessageInputDTO messageInputDTO;
    @Autowired
    public SaveMessageCommand(MessagesRepository megRepository, See_conversationsRepository seeRepository, ViewProfileMessageProducer viewProfileMessageProducer, MessageInputDTO messageInputDTO) {
        this.megRepository = megRepository;
        this.seeRepository = seeRepository;
        this.viewProfileMessageProducer = viewProfileMessageProducer;
        this.messageInputDTO = messageInputDTO;
    }

    private Message buildMessageFromSeeConversations(See_conversations seeConversations, String messageText, String messageFile) {
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);
        return Message.builder()
                .conversation_id(seeConversations.getConversation_id())
                .sent_at(timestamp)
                .message_id(UUID.randomUUID())
                .sender_id(seeConversations.getUser_id())
                .receiver_id(seeConversations.getUser_with_conversation_id())
                .sender_name(seeConversations.getUser_name())
                .receiver_name(seeConversations.getUser_with_conversation_name())
                .message_text(messageText)
                .message_file(messageFile)
                .build();
    }

    @Override
    public ResponseEntity<?> execute() {
        List<See_conversations> s1 = seeRepository.findConversationProperty(messageInputDTO.getSender_id(), messageInputDTO.getConversation_id());
        if (s1.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid sender_id or conversation_id");
        }
        See_conversations conversationsData = s1.get(0);

        if (messageInputDTO.getMessage_file() == null && messageInputDTO.getMessage_text() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("message_text and message_file fields are both null");
        }
        if (!conversationsData.getChat_open()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("this chat is closed");
        }

        Message message = buildMessageFromSeeConversations(conversationsData, messageInputDTO.getMessage_text(), messageInputDTO.getMessage_file());

        try {
            viewProfileMessageProducer.sendMessage(new NewMessageSentDTO(message.getReceiver_id(), message.getSender_id(), message.getSender_name()));
            megRepository.save(message);
            return ResponseEntity.status(HttpStatus.CREATED).body(message);

        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while saving the message");
        }
    }
}
