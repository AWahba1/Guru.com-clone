package com.messageApp.Controller;


import com.messageApp.DTO.MessageInputDTO;
import com.messageApp.DTO.UpdateDTO;
import com.messageApp.Models.Message;
import com.messageApp.Models.See_conversations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class MessageService {

    @Autowired
    private MessagesRepository MegRepository;
    @Autowired
    private See_conversationsRepository SeeRepository;

    @Autowired
    private MessageService messageService;

    public Message buidMessageFromSee_conversations(See_conversations seeConversations, String messageText,String messageFile) {
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);
        return new Message(
                seeConversations.getConversation_id(),
                timestamp,
                UUID.randomUUID(),
                seeConversations.getUser_id(),
                seeConversations.getUser_with_conversation_id(),
                seeConversations.getUser_name(),
                seeConversations.getUser_with_conversation_name(),
                messageText,
                messageFile
        );
    }

    public ResponseEntity<?> saveMessage(MessageInputDTO messageInputDTO) {
        List<See_conversations> s1 = SeeRepository.findConversationProperty(messageInputDTO.getSender_id(), messageInputDTO.getConversation_id());
        if (s1.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid sender_id or conversation_id");
        }
        See_conversations conversationsData = s1.get(0);

        if (messageInputDTO.getMessage_file() == null && messageInputDTO.getMessage_text() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("message_text and message_file fields are both null");
        }

        Message message = buidMessageFromSee_conversations(conversationsData, messageInputDTO.getMessage_text(), messageInputDTO.getMessage_file());

        try {
            MegRepository.save(message);
            return ResponseEntity.status(HttpStatus.CREATED).body(message);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while saving the message");
        }
    }


    public ResponseEntity<String> updateMessage(UpdateDTO dto) {
        if (dto.getMessage_text() == null && dto.getMessage_file() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("message_text and message_file fields are both null");
        }
        if (dto.getMessage_text().isEmpty() && dto.getMessage_file().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("message_text and message_file fields are both empty");
        }
        try {
            if (dto.getMessage_text() == null) {
                MegRepository.updateMessageFile(dto.getConversation_id(), dto.getSent_at(), dto.getMessage_id(), dto.getMessage_file());
            } else if (dto.getMessage_file() == null) {
                MegRepository.updateMessageText(dto.getConversation_id(), dto.getSent_at(), dto.getMessage_id(), dto.getMessage_text());
            } else {
                MegRepository.updateMessageBoth(dto.getConversation_id(), dto.getSent_at(), dto.getMessage_id(), dto.getMessage_text(), dto.getMessage_file());
            }
            return ResponseEntity.ok("Message updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update message");
        }
    }



}
