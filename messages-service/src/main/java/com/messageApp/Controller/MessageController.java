package com.messageApp.Controller;

import com.messageApp.Models.*;
import com.messageApp.DTO.*;
import com.messageApp.DTO.See_conversationsDTO;
import com.messageApp.DTO.UpdateDTO;
import com.messageApp.Models.Message;
import com.messageApp.Models.See_conversations;
import com.messageApp.Models.messagePrimaryKey;
import com.messageApp.command.*;
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


@RestController
@RequestMapping("/message")
public class MessageController {

    private MessageService messageService;
    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
//    public ResponseEntity<?> saveMessage(@Valid @RequestBody MessageInputDTO messageInputDTO) {
//        return messageService.saveMessage(messageInputDTO);
//    }
    public ResponseEntity<?> saveMessage(@Valid @RequestBody MessageInputDTO messageInputDTO) {
        Command saveMessageCommand = new SaveMessageCommand(messageService, messageInputDTO);
        CommandInvoker invoker = new CommandInvoker();
        invoker.setCommand(saveMessageCommand);
        invoker.executeCommand();
        return invoker.getResponse();
    }

    @GetMapping("/lists")
    public ResponseEntity<?> getMessageAll() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/chatMessages")
    public ResponseEntity<?> findMessageByCompositeKey(@RequestParam UUID conversation_id) {
        Command findMessageByCompositeKeyCommand = new FindMessageByCompositeKeyCommand(messageService, conversation_id);
        CommandInvoker invoker = new CommandInvoker();
        invoker.setCommand(findMessageByCompositeKeyCommand);
        invoker.executeCommand();
        return invoker.getResponse();
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateMessage(@Valid @RequestBody UpdateDTO dto) {
       // return messageService.updateMessage(dto);
        Command updateMessageCommand = new UpdateMessageCommand(messageService, dto);
        CommandInvoker invoker = new CommandInvoker();
        invoker.setCommand(updateMessageCommand);
        invoker.executeCommand();
        return invoker.getResponse();
    }


    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteMessage(@RequestBody messagePrimaryKey mp) {
//            return messageService.deleteMessage(mp);
        Command deleteMessageCommand = new DeleteMessageCommand(messageService, mp);
        CommandInvoker invoker = new CommandInvoker();
        invoker.setCommand(deleteMessageCommand);
        invoker.executeCommand();
        return invoker.getResponse();
    }



}
