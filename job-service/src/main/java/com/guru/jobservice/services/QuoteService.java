package com.guru.jobservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.guru.jobservice.dtos.*;
import com.guru.jobservice.helpers.AttachmentsHelper;
import com.guru.jobservice.messages.BidPlacementDto;
import com.guru.jobservice.messages.JobInvitationDto;
import com.guru.jobservice.messaging.BidPlacementMessageProducer;
import com.guru.jobservice.messaging.JobInvitationMessageProducer;
import com.guru.jobservice.model.*;
import com.guru.jobservice.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class QuoteService {
    private final QuoteRepository quoteRepository;
    private final QuoteTemplatesRepository quoteTemplatesRepository;
    private final JobWatchlistRepository jobWatchlistRepository;
    private final JobInvitationsRepository jobInvitationsRepository;
    private final JobInvitationMessageProducer jobInvitationMessageProducer;
    private final BidPlacementMessageProducer bidPlacementMessageProducer;
    private final JobRepository jobRepository;

    @Autowired
    public QuoteService(QuoteRepository quoteRepository, QuoteTemplatesRepository quoteTemplatesRepository, JobWatchlistRepository jobWatchlistRepository, JobInvitationsRepository jobInvitationsRepository, JobInvitationMessageProducer jobInvitationMessageProducer, JobRepository jobRepository, BidPlacementMessageProducer bidPlacementMessageProducer) {
        this.quoteTemplatesRepository = quoteTemplatesRepository;
        this.quoteRepository = quoteRepository;
        this.jobWatchlistRepository = jobWatchlistRepository;
        this.jobInvitationsRepository = jobInvitationsRepository;
        this.jobInvitationMessageProducer = jobInvitationMessageProducer;
        this.jobRepository = jobRepository;
        this.bidPlacementMessageProducer = bidPlacementMessageProducer;
    }

    public void addQuote(QuoteRequest quoteRequest) {
        Job job = jobRepository.getJobById(quoteRequest.getJob_id());
//        bidPlacementMessageProducer.sendMessage(new BidPlacementDto(job.getClientId(), quoteRequest.getFreelancer_id(), quoteRequest.getJob_id(),
//                job.getTitle(), quoteRequest.getProposal(), quoteRequest.getBids_used(), quoteRequest.getBid_date()));
        quoteRepository.addQuote(
                quoteRequest.getFreelancer_id(),
                quoteRequest.getJob_id(),
                quoteRequest.getProposal(),
                quoteRequest.getBids_used(),
                quoteRequest.getBid_date()
        );
    }

    public void updateQuote(QuoteRequest quoteRequest) {
        quoteRepository.updateQuote(
                quoteRequest.getId(),
                quoteRequest.getProposal(),
                quoteRequest.getBids_used(),
                quoteRequest.getQuote_status()
        );
    }

    public void addQuoteTemplate(QuoteTemplateRequest quoteTemplateRequest) throws JsonProcessingException {

        String [] attachmentJsonArray = AttachmentsHelper.convertAttachmentsToJson(quoteTemplateRequest.getAttachments());
        log.info(quoteTemplateRequest.getAttachments()[0].getUrl());
        log.info(quoteTemplateRequest.getAttachments()[0].getFilename());
        quoteTemplatesRepository.addQuoteTemplate(
                quoteTemplateRequest.getFreelancer_id(),
                quoteTemplateRequest.getTemplate_name(),
                quoteTemplateRequest.getTemplate_description(),
                attachmentJsonArray
        );
    }
    public void updateQuoteTemplate(QuoteTemplateRequest quoteTemplateRequest) throws JsonProcessingException  {
        String [] attachmentJsonArray = AttachmentsHelper.convertAttachmentsToJson(quoteTemplateRequest.getAttachments());

        quoteTemplatesRepository.updateQuoteTemplate(
                quoteTemplateRequest.getQuote_template_id(),
                quoteTemplateRequest.getTemplate_name(),
                quoteTemplateRequest.getTemplate_description(),
                attachmentJsonArray
        );
    }

    public void addJobWatchlist(UUID freelancer_id, UUID job_id) {
        jobWatchlistRepository.addJobWatchlist(freelancer_id, job_id);
    }

    public void removeJobWatchlist(UUID watchlist_id) {
        jobWatchlistRepository.removeJobWatchlist(watchlist_id);
    }

    public void inviteToJob(QuoteRequest quoteRequest, UUID client_id) {
        Job job = jobRepository.getJobById(quoteRequest.getJob_id());
        jobInvitationMessageProducer.sendMessage(new JobInvitationDto(client_id, quoteRequest.getFreelancer_id(), quoteRequest.getJob_id(), job.getTitle()));
        jobInvitationsRepository.inviteToJob(
                quoteRequest.getFreelancer_id(),
                quoteRequest.getJob_id(),
                client_id
        );
    }

    public List<Quote> getFreelancerQuotes(UUID _freelancer_id, String quote_status) {
        return quoteRepository.getFreelancerQuotes(_freelancer_id, quote_status);
    }

    public List<Quote> getFreelancerQuoteDetails(UUID _quote_id) {
        return quoteRepository.getFreelancerQuoteDetails(_quote_id);
    }

    public List<QuoteTemplates> getFreelancerQuoteTemplates(UUID _freelancer_id) {
        return quoteTemplatesRepository.getFreelancerQuoteTemplates(_freelancer_id);
    }

    public List<JobWatchlist> getFreelancerJobWatchlist(UUID _freelancer_id) {
        return jobWatchlistRepository.getFreelancerJobWatchlist(_freelancer_id);
    }

    public List<JobInvitations> getFreelancerJobInvitations(UUID _freelancer_id) {
        return jobInvitationsRepository.getFreelancerJobInvitations(_freelancer_id);
    }

    public List<BidsUsageSummary> bidsUsageSummary(UUID _freelancer_id) {
        return quoteRepository.bidsUsageSummary(_freelancer_id);
    }

    public List<BidsUsageHistory> bidsUsageHistory(UUID _freelancer_id) {
        return quoteRepository.bidsUsageHistory(_freelancer_id);
    }

    public List<LastBidsUntilThreshold> viewLastBidsUntilThreshold(UUID _freelancer_id, int threshold) {
        return quoteRepository.viewLastBidsUntilThreshold(_freelancer_id, threshold);
    }
}
