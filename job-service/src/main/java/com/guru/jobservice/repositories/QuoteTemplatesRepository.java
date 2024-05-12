package com.guru.jobservice.repositories;

import com.guru.jobservice.model.QuoteTemplates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.List;
import java.util.UUID;

public interface QuoteTemplatesRepository extends JpaRepository<QuoteTemplates, UUID> {

    @Procedure("add_quote_template")
    void addQuoteTemplate(UUID freelancer_id, String template_name, String template_description, String[] attachments);

    @Procedure("update_quote_template")
    void updateQuoteTemplate(UUID quote_template_id, String template_name, String template_description, String[] attachments);

    @Query(value = "select * from get_freelancer_quote_templates(?1)", nativeQuery = true)
    List<QuoteTemplates> getFreelancerQuoteTemplates(UUID _freelancer_id);
}
