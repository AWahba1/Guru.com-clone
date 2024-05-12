package com.guru.jobservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "job_watchlist")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JobWatchlist {

    @Id
    @GeneratedValue(
            strategy = GenerationType.UUID
    )
    UUID watchlist_id;
    UUID freelancer_id;
    UUID job_id;

    public UUID getWatchlist_id() {
        return watchlist_id;
    }

    public void setWatchlist_id(UUID watchlist_id) {
        this.watchlist_id = watchlist_id;
    }

    public UUID getFreelancer_id() {
        return freelancer_id;
    }

    public void setFreelancer_id(UUID freelancer_id) {
        this.freelancer_id = freelancer_id;
    }

    public UUID getJob_id() {
        return job_id;
    }

    public void setJob_id(UUID job_id) {
        this.job_id = job_id;
    }
}
