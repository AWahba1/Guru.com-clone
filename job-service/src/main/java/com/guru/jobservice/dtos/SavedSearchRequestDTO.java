package com.guru.jobservice.dtos;

import com.guru.jobservice.enums.JobStatus;
import com.guru.jobservice.enums.PaymentType;
import com.guru.jobservice.enums.SortOrder;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
import java.util.stream.Stream;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavedSearchRequestDTO {

    @NotBlank
    @NotNull
    private String name;

    @NotNull
    @com.guru.jobservice.validators.UUID
    private String freelancerId;

    @NotNull
    private String searchQuery;

    @com.guru.jobservice.validators.UUID
    private String categoryId;

    @com.guru.jobservice.validators.UUID
    private String subcategoryId;

    @com.guru.jobservice.validators.UUID
    private String skillId;

    private Boolean featuredOnly;

    private PaymentType paymentTerms;

    private String[] locationIds = null;

    private SortOrder sortOrder;

    private JobStatus[] statusList = null;

    private Boolean verifiedOnly;

    @Min(0)
    private Integer minEmployerSpend;

    @Min(0)
    private Integer maxQuotesReceived;

    private Boolean notViewed;

    private Boolean notApplied;

    public UUID getFreelancerId()
    {
        return UUID.fromString(this.freelancerId);
    }

    public UUID getCategoryId()
    {
        return UUID.fromString(this.categoryId);
    }

    public UUID getSubcategoryId()
    {
        return UUID.fromString(this.subcategoryId);
    }

    public UUID getSkillId()
    {
        return UUID.fromString(this.skillId);
    }

    public String[] getStatusList()
    {
        return (statusList==null || statusList.length==0)? null : Stream.of(statusList).map(JobStatus::getValue).toArray(String[]::new);
    }

    public String getPaymentTerms(){
        return paymentTerms != null ? paymentTerms.getValue() : null;
    }

    public String getSortOrder(){
        return sortOrder != null ? sortOrder.getValue() : SortOrder.NEWEST.getValue();
    }
}

