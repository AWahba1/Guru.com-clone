package com.messageApp.Controller;


import com.messageApp.DTO.MessageInputDTO;
import com.messageApp.DTO.UpdateDTO;
import com.messageApp.Models.Message;
import com.messageApp.Models.See_conversations;

import com.messageApp.Models.messagePrimaryKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MessageService {

    @Autowired
    private MessagesRepository megRepository;
    @Autowired
    private See_conversationsRepository seeRepository;

    private List<Message> cachedMessages = new ArrayList<>();

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
        List<See_conversations> s1 = seeRepository.findConversationProperty(messageInputDTO.getSender_id(), messageInputDTO.getConversation_id());
        if (s1.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid sender_id or conversation_id");
        }
        See_conversations conversationsData = s1.get(0);

        if (messageInputDTO.getMessage_file() == null && messageInputDTO.getMessage_text() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("message_text and message_file fields are both null");
        }

        Message message = buidMessageFromSee_conversations(conversationsData, messageInputDTO.getMessage_text(), messageInputDTO.getMessage_file());

        try {
            megRepository.save(message);
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
                megRepository.updateMessageFile(dto.getConversation_id(), dto.getSent_at(), dto.getMessage_id(), dto.getMessage_file());
            } else if (dto.getMessage_file() == null) {
                megRepository.updateMessageText(dto.getConversation_id(), dto.getSent_at(), dto.getMessage_id(), dto.getMessage_text());
            } else {
                megRepository.updateMessageBoth(dto.getConversation_id(), dto.getSent_at(), dto.getMessage_id(), dto.getMessage_text(), dto.getMessage_file());
            }
            return ResponseEntity.ok("Message updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update message");
        }
    }

    @Cacheable(value = "messages", key = "'allMessages'")
    public List<Message> getAllMessages() {
        return megRepository.findAll();
    }


    @Cacheable(value = "messages", key = "#conversation_id")
    public List<Message> findMessageByCompositeKey(UUID conversation_id) {
        try {
            return megRepository.findByCompositeKey(conversation_id);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public void deleteMessage(messagePrimaryKey mp) throws Exception {
        megRepository.deleteByCompositeKey(mp.getConversation_id(), mp.getSent_at(), mp.getMessage_id());
    }



//    @Cacheable(value = "messages", key = "#conversationId")
//    public List<Message> getCachedMessages(UUID conversationId) {
//        return cachedMessages;
//    }
//
//    @CacheEvict(value = "messages", key = "#conversationId")
//    public void clearCache(UUID conversationId) {
//        // This method will clear the cache for the specified conversationId
//        cachedMessages.clear();
//    }
//
//    @CachePut(value = "messages", key = "#conversationId")
//    public List<Message> cacheMessage(UUID conversationId, Message message) {
//        // Add the new message to the cached messages
//        cachedMessages.add(message);
//        return cachedMessages;
//    }
//
//    @Scheduled(fixedRate = 60000) // Run every minute
//    public void processAndSaveMessages() {
//        // Save messages to the database (batch insert or save)
//        saveMessagesToDatabase(cachedMessages);
//
//        // Clear the cache after saving
//        cachedMessages.clear();
//    }
//
//    private void saveMessagesToDatabase(List<Message> messages) {
//        // Perform batch insert or save operation to save messages to the database
//        // Example: megRepository.saveAll(messages);
//    }



}
