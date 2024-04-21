package com.guru.jobservice.repositories;

import com.guru.jobservice.dtos.JobResponse;
import com.guru.jobservice.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JobRepository extends JpaRepository<Job, UUID> {
//    @Procedure(procedureName = "get_job_by_id")
//    Job getJobById(UUID _job_id);

    @Query(value = "select * from get_job_by_id(?1)", nativeQuery = true)
    Job getJobById(UUID jobId);

    @Procedure("delete_job_by_id")
    void deleteJobById(UUID jobId);
}
//    // No Params functions
//    @Query(value = "select Gettotaljobs()", nativeQuery = true)
//    int getTotalJobs(); // returns only a number
//
//    @Procedure(procedureName = "Gettotaljobs")
//    int getTotalJobs2(); // returns only a number
//
//    // Parametric functions
//    @Query(value = "select Gettotaljobs(?1)", nativeQuery = true)
//    int getTotalJobsParam(int employeeId);
//
//    @Procedure(procedureName = "Gettotaljobs")
//    int getTotalJobsParam2(int employeeId);
//}

