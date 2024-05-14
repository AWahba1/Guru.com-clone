package com.feedbackApp.Controller;

import com.feedbackApp.DTO.MessageDTO;
import com.feedbackApp.Models.Message;
import com.feedbackApp.Models.See_conversations;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MessageService {
        public  Message buidMessageFromMessageDTO(MessageDTO messageDTO){
            return new Message(messageDTO.getConversation_id(),
                messageDTO.getSent_at(),
                UUID.randomUUID(),
                messageDTO.getSender_id(),
                messageDTO.getReceiver_id(),
                messageDTO.getSender_name(),
                messageDTO.getReceiver_name(),
                messageDTO.getMessage_text()
        );


    }

    public Message buidMessageFromSee_conversations(See_conversations seeConversations, String messageText) {
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
                messageText
        );
    }



}
