package com.feedbackApp.Controller;

import com.feedbackApp.Models.Testomonial_By_Freelancer;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.List;
import java.util.UUID;
public interface Testomonial_By_FreelancerRepository extends CassandraRepository<Testomonial_By_Freelancer,UUID> {

    @Query("SELECT * FROM Testomonial_By_Freelancer WHERE freelancer_id = ?0")
    List<Testomonial_By_Freelancer> findByCompositeKey(UUID freelancer_id);

//    @Query("UPDATE Testomonial_By_Freelancer SET review =?1 WHERE freelancer_id = ?0 ")
//    void updateTestomonial(UUID freelancer_id , String review);
//
//    @Query("DELETE FROM Testomonial_By_Freelancer WHERE client_id = ?0 AND created_at = ?1 AND feedback_id = ?2")
//    void deletefeedbackClient(UUID client_id, Timestamp created_at, UUID feedback_id);

}
