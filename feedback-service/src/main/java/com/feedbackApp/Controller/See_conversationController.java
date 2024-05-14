package com.feedbackApp.Controller;
import com.feedbackApp.DTO.*;
import com.feedbackApp.DTO.See_conversationsDTO;
import com.feedbackApp.Models.See_conversations;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/conversation")
public class See_conversationController {
    @Autowired
    private MessagesRepository MegRepository;
    @Autowired
    private See_conversationsRepository SeeRepository;

    @Autowired
    private MessageService messageService;

    @PostMapping("/AddConversation")
    public List<See_conversations> saveConversation(@RequestBody See_conversationsDTO see_conversationsDTO) {

        See_conversations conversations = See_conversationsDTO.buildSee_converstions(see_conversationsDTO);
        LocalDateTime now = LocalDateTime.now();

        Timestamp timestamp = Timestamp.valueOf(now);

        conversations.setLastEdited(timestamp);

        conversations.setChat_open(true);

        See_conversations conversations2 = new See_conversations(conversations.getUser_with_conversation_id(),
                conversations.getConversation_id(),
                timestamp,
                conversations.getUser_id(),
                conversations.getUser_with_conversation_name(),
                conversations.getUser_name(),
                true);

        List<See_conversations> seeConversationsList = new ArrayList<>();
        seeConversationsList.add(conversations);
        seeConversationsList.add(conversations2);


        try{
            return SeeRepository.saveAll(seeConversationsList);
        }
        catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    @GetMapping("/AllConversations")
    public List<See_conversations> getConversationsAll() {

        return SeeRepository.findAll();
    }

    @GetMapping("/SeeConversations")
    public List<See_conversations> findConversationByCompositeKey(@RequestParam UUID user_id) {
        try {
            return SeeRepository.findByCompositeKey(user_id);
        }catch (Exception e){
            System.out.println(e);
            return null;
        }

    }

    @GetMapping("/Search")
    public List<See_conversations> findConversationByCompositeKey(@RequestParam UUID user_id,@RequestParam String search) {
        try {
            search = "%"+search+"$";
            return SeeRepository.searchConversations(user_id,search);
        }catch (Exception e){
            System.out.println(e);
            return null;
        }

    }


    @PutMapping("/closeChat")
    public ResponseEntity<String> closeOrOpenChat(@Valid @RequestBody See_conversationCloseChatDTO conversations) {
        try {

            List<See_conversations> s1 = SeeRepository.findConversationProperty(conversations.getUser_id(),conversations.getConversation_id());
            if(s1.isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid sender_id or conversation_id");
            }
            See_conversations conversationsData =s1.get(0);

            boolean chat = conversations.getChat_open();
            SeeRepository.updateConversation(conversationsData.getUser_id(),conversationsData.getConversation_id(),chat);
            SeeRepository.updateConversation(conversationsData.getUser_with_conversation_id(),conversationsData.getConversation_id(),chat);
            return ResponseEntity.ok("Message updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update message");
        }
    }



}
