package com.feedbackApp.Controller;

import com.feedbackApp.Models.Feedback_By_Project;
import com.feedbackApp.Models.Feedback_By_Project_Pkey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface Feedback_By_ProjectRepository extends CassandraRepository<Feedback_By_Project, Feedback_By_Project_Pkey> {
    @Query("SELECT * FROM Feedback_By_Project WHERE job_id = ?0 ORDER BY created_at DESC")
    List<Feedback_By_Project> findByCompositeKey(UUID job_id );

    @Query("UPDATE Feedback_By_Project SET satisfied = ?3 WHERE job_id = ?0 AND created_at = ?1 AND feedback_id = ?2")
    void updateFeedbackProject(UUID job_id, Timestamp created_at, UUID feedback_id, Boolean satisfied);

    @Query("DELETE FROM Feedback_By_Project WHERE job_id = ?0 AND created_at = ?1 AND feedback_id  = ?2")
    void deleteFeedbackProject(UUID job_id, Timestamp created_at, UUID feedback_id);


}
