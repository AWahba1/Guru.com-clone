package com.messageApp.Controller;

import com.messageApp.Models.Feedback_By_Client;
import com.messageApp.Models.Feedback_By_Client_Pkey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.List;
import java.util.UUID;

public interface Feedback_By_ClientRepository extends CassandraRepository<Feedback_By_Client, Feedback_By_Client_Pkey> {
    @Query("SELECT * FROM Feedback_By_Client WHERE client_id = ?0 ORDER BY created_at DESC")
    List<Feedback_By_Client> findByCompositeKey(UUID client_id );

}
