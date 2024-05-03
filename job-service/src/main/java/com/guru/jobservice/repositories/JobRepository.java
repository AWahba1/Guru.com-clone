package com.guru.jobservice.repositories;

//import com.guru.jobservice.dtos.JobResponse;

import com.guru.jobservice.enums.FixedPriceRange;
import com.guru.jobservice.enums.PaymentType;
import com.guru.jobservice.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface JobRepository extends JpaRepository<Job, UUID> {

    @Query(value = "select * from get_job_by_id(?1)", nativeQuery = true)
    Job getJobById(UUID jobId);

    @Procedure("create_job")
    void createJob(String _title,
                   String _description, UUID _category_id,
                   UUID _subcategory_id, boolean _featured, UUID _client_id,
                   String _payment_type, String _fixed_price_range,
                   String _duration, String _hours_per_week,
                   BigDecimal _min_hourly_rate, BigDecimal _max_hourly_rate,
                   Date _get_quotes_until, String _visibility,
                   String[] _skills ,String[] _timezones, String[] _locations
    );

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

