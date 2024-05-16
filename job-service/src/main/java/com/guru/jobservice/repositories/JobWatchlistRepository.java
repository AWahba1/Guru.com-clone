package com.guru.jobservice.repositories;

import com.guru.jobservice.model.JobWatchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.List;
import java.util.UUID;

public interface JobWatchlistRepository extends JpaRepository<JobWatchlist, UUID> {

    @Procedure("add_job_watchlist")
    void addJobWatchlist(UUID freelancer_id, UUID job_id);

    @Procedure("remove_job_watchlist")
    void removeJobWatchlist(UUID watchlist_id);

    @Query(value = "select * from get_freelancer_job_watchlist(?1)", nativeQuery = true)
    List<JobWatchlist> getFreelancerJobWatchlist(UUID freelancer_id);

}
