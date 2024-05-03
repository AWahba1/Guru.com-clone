package com.guru.jobservice.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.guru.jobservice.enums.*;
import com.guru.jobservice.validators.ValidUUIDList;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class JobRequest {

    @NotBlank(message = "Title cannot be empty")
    @NotNull(message = "Title is required")
    private String title;

    @NotBlank(message = "Description cannot be empty")
    @NotNull(message = "Description is required")
    private String description;

    @com.guru.jobservice.validators.UUID
    @NotNull(message = "Category ID is required")
    private String categoryId;

    @com.guru.jobservice.validators.UUID
    @NotNull(message = "Subcategory ID is required")
    private String subcategoryId;


    private Boolean isFeatured = false;

    @com.guru.jobservice.validators.UUID
    private String clientId;

    @NotNull(message = "Payment Type is required")
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
    @NotNull(message = "Skills set is required")
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

    public boolean getIsFeatured(){
        return this.isFeatured != null && this.isFeatured;
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
