package com.messageApp.service;

import com.messageApp.DTO.MessageDTO;
import com.messageApp.Models.Message;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MessageService {

        public  Message buidMessage(MessageDTO messageDTO){
        Message message = new Message(messageDTO.getConversation_id(),
                messageDTO.getSent_at(),
                UUID.randomUUID(),
                messageDTO.getSender_id(),
                messageDTO.getReceiver_id(),
                messageDTO.getSender_name(),
                messageDTO.getReceiver_name(),
                messageDTO.getMessage_text()
        );

        return message;

    }
}
