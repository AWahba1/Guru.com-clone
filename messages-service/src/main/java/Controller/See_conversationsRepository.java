package Controller;

import Models.*;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface See_conversationsRepository extends CassandraRepository<See_conversations, See_conv_PrimaryKey> {
    @Query("SELECT * FROM see_conversations WHERE user_id = ?0 ORDER BY lastEdited DESC LIMIT 50")
    List<See_conversations> findByCompositeKey(UUID user_id);

    @Query("UPDATE see_conversations SET chat_open = ?3 WHERE user_id = ?0 AND lastEdited = ?1 AND conversation_id = ?2")
    void updateConversation(UUID user_id, Timestamp lastEdited, UUID conversation_id,Boolean chat_open);


    @Query("DELETE FROM see_conversations WHERE user_id = ?0 AND lastEdited = ?1 AND conversation_id = ?2")
    void deleteConversationByCompositeKey(UUID user_id, Timestamp lastEdited, UUID conversation_id);
}


