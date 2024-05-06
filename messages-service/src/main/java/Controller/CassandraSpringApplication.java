package Controller;

import Models.*;
import DTO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@SpringBootApplication
@RestController
@RequestMapping("/")

public class CassandraSpringApplication {
	@Autowired
	private MessagesRepository MegRepositoray;
	@Autowired
	private See_conversationsRepository SeeRepository;

	@Autowired
	private Search_conversationRepository SearchRepository;


	@PostMapping("/message")
	public Message saveMessage(@RequestBody MessageDTO messagesDTO)
	{
		Message message = MessageDTO.buidMessage(messagesDTO);
		LocalDateTime now = LocalDateTime.now();

		// Convert LocalDateTime to Timestamp
		Timestamp timestamp = Timestamp.valueOf(now);

		message.setSent_at(timestamp);


//		See_conversations conversations1 = new See_conversations(messages.getSender_id(),
//				timestamp,
//				messages.getConversation_id(),
//				messages.getReceiver_id(),
//				messages.getSender_name(),
//				messages.getReceiver_name());
//
//		See_conversations conversations2 = new See_conversations(messages.getReceiver_id(),
//				timestamp,
//				messages.getConversation_id(),
//				messages.getSender_id(),
//				messages.getReceiver_name(),
//				messages.getSender_name());
//
//		List<See_conversations> seeConversationsList = new ArrayList<>();
//		seeConversationsList.add(conversations1);
//		seeConversationsList.add(conversations2);

		try {
//			SeeRepository.deleteConversationByCompositeKey(messages.getSender_id(), messages.getSent_at(), messages.getConversation_id());
//			SeeRepository.deleteConversationByCompositeKey(messages.getReceiver_id(), messages.getSent_at(), messages.getConversation_id());
//			SeeRepository.saveAll(seeConversationsList);
			MegRepositoray.save(message);
			return message;

		} catch (Exception e){

			return null;
		}
	}

	@PostMapping("/AddConversation")
	public List<See_conversations> saveConversation(@RequestBody See_conversationsDTO see_conversationsDTO) {

		See_conversations conversations = See_conversationsDTO.buildSee_converstions(see_conversationsDTO);
		LocalDateTime now = LocalDateTime.now();

		// Convert LocalDateTime to Timestamp
		Timestamp timestamp = Timestamp.valueOf(now);

		conversations.setLastEdited(timestamp);

		conversations.setChat_open(true);

		See_conversations conversations2 = new See_conversations(conversations.getUser_with_conversation_id(),
				timestamp,
				conversations.getConversation_id(),
				conversations.getUser_id(),
				conversations.getUser_with_conversation_name(),
				conversations.getUser_name(),
				true);

		List<See_conversations> seeConversationsList = new ArrayList<>();
		seeConversationsList.add(conversations);
		seeConversationsList.add(conversations2);


		Search_conversation search1 =new Search_conversation(conversations.getUser_id(),
				timestamp,
				conversations.getConversation_id(),
				conversations.getUser_with_conversation_id(),
				conversations.getUser_name(),
				conversations.getUser_with_conversation_name(),
				true);

		Search_conversation search2 = new Search_conversation(conversations.getUser_with_conversation_id(),
				timestamp,
				conversations.getConversation_id(),
				conversations.getUser_id(),
				conversations.getUser_with_conversation_name(),
				conversations.getUser_name(),
				true);

		List<Search_conversation> searchConversations = new ArrayList<>();
		searchConversations.add(search1);
		searchConversations.add(search2);

		try{
			SearchRepository.saveAll(searchConversations);
			return SeeRepository.saveAll(seeConversationsList);
		}
		catch (Exception e){
			System.out.println(e);
			return null;
		}
	}


	@GetMapping("/lists")
	public List<Message> getMessageAll() {
		return MegRepositoray.findAll();
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
	public List<Search_conversation> findConversationByCompositeKey(@RequestParam UUID user_id,@RequestParam String search) {
		try {
			search = "%"+search+"$";
			return SeeRepository.searchConversations(user_id,search);
		}catch (Exception e){
			System.out.println(e);
			return null;
		}

	}

	@GetMapping("/listsbyid")
	public List<Message> findMessageByCompositeKey(@RequestParam UUID conversation_id) {
		try {
			return MegRepositoray.findByCompositeKey(conversation_id);
		}
		catch (Exception e){
			System.out.println(e);
			return null;
		}

	}

	@PutMapping("/update")
	public ResponseEntity<String> updateMessage(@RequestBody UpdateDTO dto) {
		try {
			MegRepositoray.updateMessage(dto.getConversation_id(), dto.getSent_at(), dto.getMessage_id(), dto.getMessage_text());
			return ResponseEntity.ok("Message updated successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update message");
		}
	}

	@PutMapping("/closeChat")
	public ResponseEntity<String> closeOrOpenChat(@RequestBody See_conversations conversations) {
		try {
			boolean chat = conversations.getChat_open();
			SeeRepository.updateConversation(conversations.getUser_id(),conversations.getLastEdited(),conversations.getConversation_id(),chat);
			SeeRepository.updateConversation(conversations.getUser_with_conversation_id(),conversations.getLastEdited(),conversations.getConversation_id(),chat);
			return ResponseEntity.ok("Message updated successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update message");
		}
	}


	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteMessage(@RequestBody messagePrimaryKey mp) {
		try {
			MegRepositoray.deleteByCompositeKey(mp.getConversation_id(), mp.getSent_at(), mp.getMessage_id());
			return ResponseEntity.ok("Message deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete message");
		}
	}

		public static void main(String[] args) {
		SpringApplication.run(CassandraSpringApplication.class, args);
	}

}
