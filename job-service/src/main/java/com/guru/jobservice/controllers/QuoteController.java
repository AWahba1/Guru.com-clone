package com.guru.jobservice.controllers;

import com.guru.jobservice.dtos.*;
import com.guru.jobservice.model.JobInvitations;
import com.guru.jobservice.model.JobWatchlist;
import com.guru.jobservice.model.Quote;
import com.guru.jobservice.model.QuoteTemplates;
import com.guru.jobservice.services.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/quotes")
public class QuoteController {
    private final QuoteService quoteService;

    @Autowired
    public QuoteController(QuoteService quoteService) { this.quoteService = quoteService; }

    // DONE
    @PostMapping("/addQuote")
    public ResponseEntity<String> addQuote(@RequestBody QuoteRequest quoteRequest) {
        quoteService.addQuote(quoteRequest);
        return new ResponseEntity<>("Quote added successfully", HttpStatus.CREATED);
    }

    // DONE
    @PutMapping("/updateQuote")
    public ResponseEntity<String> updateQuote(@RequestBody QuoteRequest quoteRequest) {
        quoteService.updateQuote(quoteRequest);
        return new ResponseEntity<>("Quote updated successfully", HttpStatus.OK);
    }

    // DONE
    @PostMapping("/addQuoteTemplate")
    public ResponseEntity<String> addQuoteTemplate(@RequestBody QuoteTemplateRequest quoteTemplateRequest) {
        quoteService.addQuoteTemplate(quoteTemplateRequest);
        return new ResponseEntity<>("Quote Template added successfully", HttpStatus.CREATED);
    }

    // DONE
    @PutMapping("/updateQuoteTemplate")
    public ResponseEntity<String> updateQuoteTemplate(@RequestBody QuoteTemplateRequest quoteTemplateRequest) {
        quoteService.updateQuoteTemplate(quoteTemplateRequest);
        return new ResponseEntity<>("Quote Template updated successfully", HttpStatus.OK);
    }

    // DONE
    @PostMapping("/addJobWatchlist")
    public ResponseEntity<String> addJobWatchlist(@RequestBody QuoteRequest quoteRequest) {
        quoteService.addJobWatchlist(quoteRequest.getFreelancer_id(), quoteRequest.getJob_id());
        return new ResponseEntity<>("Job Watchlist added successfully", HttpStatus.CREATED);
    }

    // DONE
    @DeleteMapping("/removeJobWatchlist/{watchlist_id}")
    public ResponseEntity<String> removeJobWatchlist(@PathVariable UUID watchlist_id) {
        quoteService.removeJobWatchlist(watchlist_id);
        return new ResponseEntity<>("Job Watchlist removed successfully", HttpStatus.OK);
    }

    // DONE
    @PostMapping("/inviteToJob/{client_id}")
    public ResponseEntity<String> inviteToJob(@RequestBody QuoteRequest quoteRequest, @PathVariable UUID client_id) {
        quoteService.inviteToJob(quoteRequest, client_id);
        return new ResponseEntity<>("Job invitation sent successfully", HttpStatus.CREATED);
    }

    // DONE
    @GetMapping("/getFreelancerQuotes")
    public ResponseEntity<List<Quote>> getFreelancerQuotes(@RequestBody QuoteRequest quoteRequest) {
        List<Quote> freelancerQuotes = quoteService.getFreelancerQuotes(quoteRequest.getFreelancer_id(), quoteRequest.getQuote_status());
        if(freelancerQuotes != null)
            return new ResponseEntity<>(freelancerQuotes, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // DONE
    @GetMapping("/getFreelancerQuoteDetails/{freelancer_id}")
    public ResponseEntity<List<Quote>> getFreelancerQuoteDetails(@PathVariable UUID freelancer_id) {
        List<Quote> freelancerQuotesDetails = quoteService.getFreelancerQuoteDetails(freelancer_id);
        if(freelancerQuotesDetails != null)
            return new ResponseEntity<>(freelancerQuotesDetails, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // DONE
    @GetMapping("/getFreelancerQuoteTemplates/{freelancer_id}")
    public ResponseEntity<List<QuoteTemplates>> getFreelancerQuoteTemplates(@PathVariable UUID freelancer_id) {
        List<QuoteTemplates> data = quoteService.getFreelancerQuoteTemplates(freelancer_id);
        if(data != null)
            return new ResponseEntity<>(data, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // DONE
    @GetMapping("/getFreelancerJobWatchlist/{freelancer_id}")
    public ResponseEntity<List<JobWatchlist>> getFreelancerJobWatchlist(@PathVariable UUID freelancer_id) {
        List<JobWatchlist> data = quoteService.getFreelancerJobWatchlist(freelancer_id);
        if(data != null)
            return new ResponseEntity<>(data, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // DONE
    @GetMapping("/getFreelancerJobInvitations/{_freelancer_id}")
    public ResponseEntity<List<JobInvitations>> getFreelancerJobInvitations(@PathVariable UUID _freelancer_id) {
        List<JobInvitations> data = quoteService.getFreelancerJobInvitations(_freelancer_id);
        if(data != null)
            return new ResponseEntity<>(data, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // DONE
    @GetMapping("/bidsUsageSummary/{freelancer_id}")
    public ResponseEntity<List<BidsUsageSummary>> bidsUsageSummary(@PathVariable UUID freelancer_id) {
        List<BidsUsageSummary> data = quoteService.bidsUsageSummary(freelancer_id);
        if(data != null)
            return new ResponseEntity<>(data, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // DONE
    @GetMapping("/bidsUsageHistory/{_freelancer_id}")
    public ResponseEntity<List<BidsUsageHistory>> bidsUsageHistory(@PathVariable UUID _freelancer_id) {
        List<BidsUsageHistory> data = quoteService.bidsUsageHistory(_freelancer_id);
        if(data != null)
            return new ResponseEntity<>(data, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // DONE
    @GetMapping("/viewLastBidsUntilThreshold/{freelancer_id}/{threshold}")
    public ResponseEntity<List<LastBidsUntilThreshold>> viewLastBidsUntilThreshold(@PathVariable UUID freelancer_id, @PathVariable int threshold) {
        List<LastBidsUntilThreshold> data = quoteService.viewLastBidsUntilThreshold(freelancer_id, threshold);
        if (data != null)
            return new ResponseEntity<>(data, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
