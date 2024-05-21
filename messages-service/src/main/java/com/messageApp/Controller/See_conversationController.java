package com.messageApp.Controller;
import com.messageApp.Models.*;
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

@RestController
@RequestMapping("/conversation")
public class See_conversationController {

    private See_conversationService seeConversationService;
    @Autowired
    public See_conversationController(See_conversationService seeConversationService) {
        this.seeConversationService = seeConversationService;
    }

    @PostMapping("/AddConversation")
    public ResponseEntity<?> saveConversation(@RequestBody See_conversationsDTO see_conversationsDTO) {
        return seeConversationService.saveConversation(see_conversationsDTO);
    }

    @GetMapping("/AllConversations")
    public List<See_conversations> getConversationsAll() {
        return seeConversationService.getConversationsAll();
    }

    @GetMapping("/SeeConversations")
    public List<See_conversations> findConversationByCompositeKey(@RequestParam UUID user_id) {
        return seeConversationService.findConversationByCompositeKey(user_id);
    }

    @GetMapping("/Search")
    public List<See_conversations> findConversationByCompositeKey(@RequestParam UUID user_id, @RequestParam String search) {
        return seeConversationService.searchConversations(user_id, search);
    }

    @PutMapping("/closeChat")
    public ResponseEntity<String> closeOrOpenChat(@Valid @RequestBody See_conversationCloseChatDTO conversations) {
        return seeConversationService.closeOrOpenChat(conversations);
    }

}
