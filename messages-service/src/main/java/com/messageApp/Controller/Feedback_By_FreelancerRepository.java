package com.messageApp.Controller;
import com.messageApp.Models.*;

import com.messageApp.Models.Feedback_By_Freelancer;
import com.messageApp.Models.Feedback_By_Freelancer_PKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.List;
import java.util.UUID;
public interface Feedback_By_FreelancerRepository extends CassandraRepository<Feedback_By_Freelancer, Feedback_By_Freelancer_PKey> {
    @Query("SELECT * FROM Feedback_By_Freelancer WHERE freelancer_id = ?0 ORDER BY created_at DESC")
    List<Feedback_By_Freelancer> findByCompositeKey(UUID freelancer_id);

}
