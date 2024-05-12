package com.guru.jobservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuoteTemplateRequest {

    private UUID quote_template_id;
    private UUID freelancer_id;
    private String template_name;
    private String template_description;
    private String[] attachments;

    public UUID getQuote_template_id() {
        return quote_template_id;
    }

    public void setQuote_template_id(UUID quote_template_id) {
        this.quote_template_id = quote_template_id;
    }

    public UUID getFreelancer_id() {
        return freelancer_id;
    }

    public void setFreelancer_id(UUID freelancer_id) {
        this.freelancer_id = freelancer_id;
    }

    public String getTemplate_name() {
        return template_name;
    }

    public void setTemplate_name(String template_name) {
        this.template_name = template_name;
    }

    public String getTemplate_description() {
        return template_description;
    }

    public void setTemplate_description(String template_description) {
        this.template_description = template_description;
    }

    public String[] getAttachments() {
        return attachments;
    }

    public void setAttachments(String[] attachments) {
        this.attachments = attachments;
    }
}
