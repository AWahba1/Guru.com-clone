package com.messageApp.Controller;

import org.springframework.stereotype.Service;
import com.messageApp.DTO.*;
import com.messageApp.DTO.See_conversationsDTO;
import com.messageApp.Models.See_conversations;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
@Service
public class See_conversationService {
    private See_conversationsRepository SeeRepository;

    @Autowired
    public See_conversationService(See_conversationsRepository seeRepository) {
        SeeRepository = seeRepository;
    }

    public ResponseEntity<?> saveConversation(See_conversationsDTO see_conversationsDTO) {
        if (see_conversationsDTO.getUser_role() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user_role should be client or freelancer");
        }

        if (!see_conversationsDTO.getUser_role().equalsIgnoreCase("client") && !see_conversationsDTO.getUser_role().equalsIgnoreCase("freelancer")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user_role should be client or freelancer");
        }

        See_conversations conversations = See_conversationsDTO.buildSee_converstions(see_conversationsDTO);
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);

        conversations.setLastEdited(timestamp);
        conversations.setChat_open(true);
        String role = "client";
        if (see_conversationsDTO.getUser_role().equalsIgnoreCase("client")) {
            role = "freelancer";
        }
        See_conversations conversations2 = new See_conversations(conversations.getUser_with_conversation_id(),
                conversations.getConversation_id(),
                timestamp,
                conversations.getUser_id(),
                conversations.getUser_with_conversation_name(),
                conversations.getUser_name(),
                true, role);

        List<See_conversations> seeConversationsList = new ArrayList<>();
        seeConversationsList.add(conversations);
        seeConversationsList.add(conversations2);

        try {
            SeeRepository.saveAll(seeConversationsList);
            return ResponseEntity.status(HttpStatus.CREATED).body(seeConversationsList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while saving the message");
        }
    }


    public List<See_conversations> getConversationsAll() {
        return SeeRepository.findAll();
    }


    @Cacheable(value = "conversations", key = "#user_id")
    public List<See_conversations> findConversationByCompositeKey(UUID user_id) {
        try {
            return SeeRepository.findByCompositeKey(user_id);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }


    public List<See_conversations> searchConversations(UUID user_id, String search) {
        try {
            search = "%" + search + "%";
            return SeeRepository.searchConversations(user_id, search);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }


    public ResponseEntity<String> closeOrOpenChat(See_conversationCloseChatDTO conversations) {
        try {
            List<See_conversations> s1 = SeeRepository.findConversationProperty(conversations.getUser_id(), conversations.getConversation_id());
            if (s1.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid sender_id or conversation_id");
            }
            See_conversations conversationsData = s1.get(0);

            if (conversationsData.getUser_role().equalsIgnoreCase("freelancer")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("only client can close or open chat");
            }

            boolean chat = conversations.getChat_open();
            SeeRepository.updateConversation(conversationsData.getUser_id(), conversationsData.getConversation_id(), chat);
            SeeRepository.updateConversation(conversationsData.getUser_with_conversation_id(), conversationsData.getConversation_id(), chat);
            return ResponseEntity.ok("conversation updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update conversation");
        }
    }
}
