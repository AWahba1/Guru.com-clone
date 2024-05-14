package com.messageApp.Controller;

import com.messageApp.Models.Feedback_By_Client;
import com.messageApp.Models.Feedback_By_Client_Pkey;
import com.messageApp.Models.Feedback_By_Project;
import com.messageApp.Models.Reports_By_Job;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface Reports_By_JobRepository extends CassandraRepository<Reports_By_Job, UUID> {
    @Query("SELECT * FROM Reports_By_Job WHERE job_id = ?0")
    List<Reports_By_Job> findByCompositeKey(UUID job_id);

    @Query("Delete FROM Reports_By_Job WHERE job_id = ?0")
    void deleteReport(UUID job_id);

    @Query("UPDATE Reports_By_Job SET report = ?1, job_status = ?2 WHERE job_id = ?0")
    void UpdateReportBoth(UUID job_id ,String report, String job_status);

    @Query("UPDATE Reports_By_Job SET report =?1 WHERE job_id = ?0")
    void UpdateReportReport(UUID job_id ,String report);

    @Query("UPDATE Reports_By_Job SET job_status = ?1 WHERE job_id = ?0")
    void UpdateReportJobStatus(UUID job_id , String job_status);

}
