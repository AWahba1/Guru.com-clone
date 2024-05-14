package com.feedbackApp.Controller;

import com.feedbackApp.Models.See_conv_PrimaryKey;
import com.feedbackApp.Models.See_conversations;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.List;
import java.util.UUID;

public interface See_conversationsRepository extends CassandraRepository<See_conversations, See_conv_PrimaryKey> {
    @Query("SELECT * FROM see_conversations WHERE user_id = ?0")
    List<See_conversations> findByCompositeKey(UUID user_id);

    @Query("SELECT * FROM see_conversations WHERE user_id = ?0 AND conversation_id = ?1 LIMIT 1")
    List<See_conversations> findConversationProperty(UUID user_id,UUID conversation_id);
    @Query("SELECT * FROM see_conversations WHERE user_id = ?0 AND user_with_conversation_name LIKE ?1")
    List<See_conversations> searchConversations(UUID user_id,String user_with_conversation_name);

    @Query("UPDATE see_conversations SET chat_open = ?2 WHERE user_id = ?0 AND conversation_id = ?1")
    void updateConversation(UUID user_id,UUID conversation_id,Boolean chat_open);


    @Query("DELETE FROM see_conversations WHERE user_id = ?0 AND conversation_id = ?1")
    void deleteConversationByCompositeKey(UUID user_id, UUID conversation_id);
}


