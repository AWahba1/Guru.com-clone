package com.guru.jobservice.repositories;

import com.guru.jobservice.model.JobInvitations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.List;
import java.util.UUID;

public interface JobInvitationsRepository extends JpaRepository<JobInvitations, UUID> {

    @Procedure("invite_to_job")
    void inviteToJob(UUID freelancer_id, UUID client_id, UUID job_id);

    @Query(value = "select * from get_freelancer_job_invitations(?1)", nativeQuery = true)
    List<JobInvitations> getFreelancerJobInvitations(UUID _freelancer_id);
}
