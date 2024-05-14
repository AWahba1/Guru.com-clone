package com.messageApp.Controller;

import com.messageApp.Models.*;
import com.messageApp.DTO.*;
import com.messageApp.DTO.MessageDTO;
import com.messageApp.DTO.See_conversationsDTO;
import com.messageApp.DTO.UpdateDTO;
import com.messageApp.Models.Message;
import com.messageApp.Models.See_conversations;
import com.messageApp.Models.messagePrimaryKey;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;


@SpringBootApplication
@RestController
@RequestMapping("/")

public class CassandraSpringApplication {
	@Autowired
	private MessagesRepository MegRepository;
	@Autowired
	private See_conversationsRepository SeeRepository;

	@Autowired
	private MessageService messageService;

	@PostMapping("/message")
	public ResponseEntity<?> saveMessage(@Valid @RequestBody MessageInputDTO messageInputDTO)
	{

		List<See_conversations> s1 = SeeRepository.findConversationProperty(messageInputDTO.getSender_id(),messageInputDTO.getConversation_id());
		if(s1.isEmpty()){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid sender_id or conversation_id");
		}
		See_conversations conversationsData =s1.get(0);

		Message message = messageService.buidMessageFromSee_conversations(conversationsData,messageInputDTO.getMessage_text());

		try {
			MegRepository.save(message);
			return ResponseEntity.status(HttpStatus.CREATED).body(message);

		} catch (Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while saving the message");
		}
	}


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


	@GetMapping("/lists")
	public List<Message> getMessageAll() {
		return MegRepository.findAll();
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

	@GetMapping("/chatMessages")
	public List<Message> findMessageByCompositeKey(@RequestParam UUID conversation_id) {
		try {
			return MegRepository.findByCompositeKey(conversation_id);
		}
		catch (Exception e){
			System.out.println(e);
			return null;
		}

	}

	@PutMapping("/update")
	public ResponseEntity<String> updateMessage(@Valid @RequestBody UpdateDTO dto) {
		try {
			MegRepository.updateMessage(dto.getConversation_id(), dto.getSent_at(), dto.getMessage_id(), dto.getMessage_text());
			return ResponseEntity.ok("Message updated successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update message");
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


	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteMessage(@RequestBody messagePrimaryKey mp) {
		try {
			MegRepository.deleteByCompositeKey(mp.getConversation_id(), mp.getSent_at(), mp.getMessage_id());
			return ResponseEntity.ok("Message deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete message");
		}
	}

		public static void main(String[] args) {
		SpringApplication.run(CassandraSpringApplication.class, args);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(
			MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}

}
