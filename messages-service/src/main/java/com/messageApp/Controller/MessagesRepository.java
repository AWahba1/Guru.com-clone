package com.messageApp.Controller;

import com.messageApp.Models.*;

import com.messageApp.Models.Message;
import com.messageApp.Models.messagePrimaryKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;


public interface MessagesRepository extends CassandraRepository<Message, messagePrimaryKey> {
    @Query("SELECT * FROM message WHERE conversation_id = ?0 ORDER BY sent_at DESC")
    List<Message> findByCompositeKey(UUID conversationId);

    @Query("UPDATE message SET message_text = ?3 WHERE conversation_id = ?0 AND sent_at = ?1 AND message_id = ?2")
    void updateMessage(UUID conversationId, Timestamp sentAt, UUID messageId, String messageText);

    @Query("DELETE FROM message WHERE conversation_id = ?0 AND sent_at = ?1 AND message_id = ?2")
    void deleteByCompositeKey(UUID conversationId, Timestamp sentAt, UUID messageId);
}