package com.messageApp.Controller;

import com.messageApp.Models.*;
import com.messageApp.DTO.*;
import com.messageApp.DTO.See_conversationsDTO;
import com.messageApp.DTO.UpdateDTO;
import com.messageApp.Models.Message;
import com.messageApp.Models.See_conversations;
import com.messageApp.Models.messagePrimaryKey;
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


    public MessageController(MessageService messageService) {
        this.messageService = messageService;


    }

    @PostMapping
    public ResponseEntity<?> saveMessage(@Valid @RequestBody MessageInputDTO messageInputDTO) {
        Command command = new SaveMessageCommand(
                messageService.getMegRepository(),
                messageService.getSeeRepository(),
                messageService.getViewProfileMessageProducer(),
                messageInputDTO
        );
        return command.execute();
    }
//    public ResponseEntity<?> saveMessage(@Valid @RequestBody MessageInputDTO messageInputDTO) {
//        return messageService.saveMessage(messageInputDTO);
//    }


    @GetMapping("/lists")
    public ResponseEntity<?> getMessageAll() {
        Command command = new GetAllMessagesCommand();
        return command.execute();
    }
//    public List<Message> getMessageAll() {
//        return messageService.getAllMessages();
//    }

    @GetMapping("/chatMessages")
    public ResponseEntity<?> findMessageByCompositeKey(@RequestParam UUID conversation_id) {
        Command command = new FindMessageByCompositeKeyCommand(messageService.getMegRepository(), conversation_id);
        return command.execute();
    }
//    public List<Message> findMessageByCompositeKey(@RequestParam UUID conversation_id) {
//        return messageService.findMessageByCompositeKey(conversation_id);
//    }

    @PutMapping("/update")
    public ResponseEntity<?> updateMessage(@Valid @RequestBody UpdateDTO dto) {
        Command command = new UpdateMessageCommand(messageService.getMegRepository(), dto);
        return command.execute();
    }
//    public ResponseEntity<String> updateMessage(@Valid @RequestBody UpdateDTO dto) {
//        return messageService.updateMessage(dto);
//    }


    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteMessage(@RequestBody messagePrimaryKey mp) {
        Command command = new DeleteMessageCommand(messageService.getMegRepository(), mp);
        return command.execute();
    }
//    public ResponseEntity<String> deleteMessage(@RequestBody messagePrimaryKey mp) {
//        try {
//            messageService.deleteMessage(mp);
//            return ResponseEntity.ok("Message deleted successfully");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete message");
//        }
//    }

}
