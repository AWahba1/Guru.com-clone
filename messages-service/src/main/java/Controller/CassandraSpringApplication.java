package Controller;

import Models.*;
import DTO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
	private MessagesRepository repository;


	@PostMapping("/message")
	public Message save(@RequestBody Message messages) {
		return repository.save(messages);
	}



	@GetMapping("/lists")
	public List<Message> getAll() {
		return repository.findAll();
	}

	@GetMapping("/listsbyid")
	public List<Message> findMessageByCompositeKey(@RequestParam UUID conversation_id) {
		return repository.findByCompositeKey(conversation_id);
	}

	@PutMapping("/update")
	public ResponseEntity<String> updateMessage(@RequestBody UpdateDTO dto) {
		try {
			repository.updateMessage(dto.getConversation_id(), dto.getSent_at(), dto.getMessage_id(), dto.getMessage_text());
			return ResponseEntity.ok("Message updated successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update message");
		}
	}


	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteMessage(@RequestBody messagePrimaryKey mp) {
		try {
			repository.deleteByCompositeKey(mp.getConversation_id(), mp.getSent_at(), mp.getMessage_id());
			return ResponseEntity.ok("Message deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete message");
		}
	}



		public static void main(String[] args) {
		SpringApplication.run(CassandraSpringApplication.class, args);
	}

}
