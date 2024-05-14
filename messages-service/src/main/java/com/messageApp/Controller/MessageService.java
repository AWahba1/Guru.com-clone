package com.messageApp.Controller;

import com.messageApp.DTO.MessageDTO;
import com.messageApp.DTO.MessageInputDTO;
import com.messageApp.Models.Message;
import com.messageApp.Models.See_conversations;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
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
