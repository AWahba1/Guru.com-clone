package com.guru.jobservice.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guru.jobservice.enums.*;
import com.guru.jobservice.validators.ValidUUIDList;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
//import jakarta.validation.constraints.Email;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class JobRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @com.guru.jobservice.validators.UUID
    private String categoryId;

    @com.guru.jobservice.validators.UUID
    private String subcategoryId;

    private Boolean isFeatured;

    @com.guru.jobservice.validators.UUID
    private String clientId;

    private PaymentType paymentType;
    private FixedPriceRange fixedPriceRange;
    private JobDuration duration;
    private HoursPerWeek hoursPerWeek;

    @Min(0)
    private BigDecimal minHourlyRate;
    @Min(0)
    private BigDecimal maxHourlyRate;

    @FutureOrPresent
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date getQuotesUntil;
    private JobVisibility visibility;
    private JobStatus status;

    @ValidUUIDList
    private String[] skills;

    @ValidUUIDList
    private String[] timezones;

    @ValidUUIDList
    private String[] locations;



    public UUID getClientId()
    {
        return UUID.fromString(this.clientId);
    }

    public UUID getCategoryId()
    {
        return UUID.fromString(this.categoryId);
    }
    public UUID getSubcategoryId()
    {
        return UUID.fromString(this.subcategoryId);
    }

    public String getPaymentType() {
        return this.paymentType != null ? this.paymentType.getValue() : null;

    }

    public String getFixedPriceRange() {
        return this.fixedPriceRange != null ? this.fixedPriceRange.getValue() : null;

    }

    public String getDuration() {
        return this.duration != null ? this.duration.getValue() : null;

    }

    public String getHoursPerWeek() {
        return hoursPerWeek != null ? hoursPerWeek.getValue() : null;
    }

    public String getVisibility() {
        return this.visibility != null ? this.visibility.getValue() : null;
    }

    public String getStatus() {
        return this.status != null ? this.status.getValue() : null;
    }

}
