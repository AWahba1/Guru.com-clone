package com.messageApp.Controller;
import com.messageApp.Models.*;

import com.messageApp.Models.Feedback_By_Freelancer;
import com.messageApp.Models.Feedback_By_Freelancer_PKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
public interface Feedback_By_FreelancerRepository extends CassandraRepository<Feedback_By_Freelancer, Feedback_By_Freelancer_PKey> {
    @Query("SELECT * FROM Feedback_By_Freelancer WHERE freelancer_id = ?0 ORDER BY created_at DESC")
    List<Feedback_By_Freelancer> findByCompositeKey(UUID freelancer_id);

    @Query("UPDATE Feedback_By_Freelancer SET satisfied = ?3 WHERE freelancer_id = ?0 AND created_at = ?1 AND feedback_id = ?2")
    void updateFeedbackFreelancer(UUID freelancer_id,Timestamp created_at, UUID feedback_id, Boolean satisfied);

    @Query("DELETE FROM Feedback_By_Freelancer WHERE freelancer_id = ?0 AND created_at = ?1 AND feedback_id = ?2")
    void deleteFeedbackFreelancer(UUID freelancer_id, Timestamp created_at, UUID feedback_id);



}
