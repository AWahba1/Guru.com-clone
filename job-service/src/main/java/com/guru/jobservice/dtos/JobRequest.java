package com.guru.jobservice.dtos;

import com.guru.jobservice.enums.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobRequest {
    private String title;
    private String description;
    private UUID categoryId;
    private UUID subcategoryId;
    private boolean featured;
    private UUID clientId;
    private PaymentType paymentType;
    private FixedPriceRange fixedPriceRange;
    private JobDuration duration;
    private HoursPerWeek hoursPerWeek;
    private BigDecimal minHourlyRate;
    private BigDecimal maxHourlyRate;
    private Date getQuotesUntil;
    private JobVisibility visibility;
    private JobStatus status;
    private String[] skills;
    private String[] timezones;
    private String[] locations;


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
