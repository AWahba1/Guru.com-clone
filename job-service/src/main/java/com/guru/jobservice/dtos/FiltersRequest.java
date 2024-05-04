package com.guru.jobservice.dtos;

import com.guru.jobservice.enums.JobStatus;
import com.guru.jobservice.enums.PaymentType;
import com.guru.jobservice.enums.SortOrder;
import com.guru.jobservice.validators.ValidUUIDList;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;
import java.util.stream.Stream;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiltersRequest {

    @PositiveOrZero(message = "Page number must be positive or zero")
    Integer page;

    @PositiveOrZero(message = "Page size must be positive or zero")
    Integer pageSize;

    String searchQuery;

    @com.guru.jobservice.validators.UUID
    String categoryId;

    @com.guru.jobservice.validators.UUID
    String subcategoryId;

    @com.guru.jobservice.validators.UUID
    String skillId;

    Boolean featuredOnly;

    PaymentType paymentType;

    @ValidUUIDList
    String[] locationIds;

    SortOrder sortOrder;

    JobStatus[] statusList;
    Boolean verifiedOnlyClients;

    @Min(0)
    Integer minEmployerSpend;

    @Min(0)
    Integer maxQuotesReceived;

    Boolean notViewed;

    Boolean notApplied;

    @com.guru.jobservice.validators.UUID
    String freelancerId;

    @com.guru.jobservice.validators.UUID
    String clientId;

    public String getPaymentType(){
        return paymentType != null ? paymentType.getValue() : null;
    }

    public String getSortOrder(){
        return sortOrder != null ? sortOrder.getValue() : SortOrder.NEWEST.getValue();
    }

    public UUID getCategoryId(){
        return categoryId!=null ? UUID.fromString(categoryId) : null;
    }

    public UUID getSubcategoryId(){
        return subcategoryId!=null ? UUID.fromString(subcategoryId) : null;
    }

    public UUID getSkillId(){
        return skillId!=null ? UUID.fromString(skillId) : null;
    }

//    public UUID getLocationId(){
//        return locationId!=null ? UUID.fromString(locationId) : null;
//    }

    public UUID getFreelancerId(){
        return freelancerId!=null ? UUID.fromString(freelancerId) : null;
    }

    public UUID getClientId(){
        return clientId!=null ? UUID.fromString(clientId) : null;
    }

    public String[] getStatusList()
    {
        return (statusList==null || statusList.length==0)? null : Stream.of(statusList).map(JobStatus::getValue).toArray(String[]::new);
    }

    public Integer getPage(){
        return this.page != null ? this.page : 1;
    }

    public Integer getPageSize(){
        return this.pageSize != null ? this.pageSize : 10;
    }

}
