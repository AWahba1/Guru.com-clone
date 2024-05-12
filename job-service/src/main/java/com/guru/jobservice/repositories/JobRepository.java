package com.guru.jobservice.repositories;

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
                   String[] _skills ,String[] _timezones, String[] _locations, String[] _attachments
    );

    @Procedure("update_job")
    void updateJob(UUID _jobId, String _title, String _description,
                   UUID _category_id, UUID _subcategory_id, Boolean _featured,
                   String _payment_type, String _fixed_price_range,
                   String _duration, String _hours_per_week,
                   BigDecimal _min_hourly_rate, BigDecimal _max_hourly_rate,
                   Date _get_quotes_until, String _visibility, String status,
                   String[] _skills ,String[] _timezones, String[] _locations
    );

    @Query(value = "select * from get_all_jobs(?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13, ?14, ?15, ?16, ?17, ?18)", nativeQuery = true)
    List<Job> getAllJobs(
                        int _page, int _page_size,
                         String _search_query,
                         UUID _category_id, UUID _subcategory_id, UUID _skill_id, Boolean _featured_only,
                         String _payment_terms, String[] _location_ids, String _sort_order, String[] _status_list,
                         Boolean _verified_only, Integer _min_employer_spend,
                         Integer _max_quotes_received, Boolean _not_viewed, Boolean _not_applied, UUID _freelancer_id,
                         UUID _client_id
    );

    @Procedure("delete_job_by_id")
    void deleteJobById(UUID jobId);

    @Procedure("view_job")
    void markJobAsViewed(UUID _job_id, UUID _freelancer_id);
}


