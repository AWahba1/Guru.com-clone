package Controller;

import Models.Search_conv_PrimaryKey;
import Models.Search_conversation;
import Models.See_conv_PrimaryKey;
import Models.See_conversations;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface Search_conversationRepository extends CassandraRepository<Search_conversation, Search_conv_PrimaryKey> {

    @Query("SELECT * FROM search_conversation WHERE user_id = ?0 AND user_with_conversation_name LIKE ?1")
    List<Search_conversation> findByCompositeKey(UUID user_id,String user_with_conversation_name);

    @Query("DELETE FROM search_conversation WHERE user_id = ?0 AND user_with_conversation_name?1 AND lastEdited = ?2 AND conversation_id = ?3")
    void deleteSearchConversationByCompositeKey(UUID user_id, String user_with_conversation_name, Timestamp lastEdited, UUID conversation_id);
}



