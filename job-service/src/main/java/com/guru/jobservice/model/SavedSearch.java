package com.guru.jobservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "saved_searches")
public class SavedSearch {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

//    @Column(name = "name")
    private String name;

//    @Column(name = "search_query")
    private String searchQuery;

//    @Column(name = "category_id")
    private UUID categoryId;

//    @Column(name = "subcategory_id")
    private UUID subcategoryId;

//    @Column(name = "skill_id")
    private UUID skillId;

//    @Column(name = "featured_only")
    private Boolean featuredOnly;

//    @Enumerated(EnumType.STRING)
    @Column(name = "payment_terms")
    private String paymentType;

//    @ElementCollection
//    @Column(name = "location_ids")
    private List<UUID> locationIds;

//    @Column(name = "sort_order")
    private String sortOrder;

//    @ElementCollection
//    @Column(name = "status_list")
    private List<String> statusList;

//    @Column(name = "verified_only")
    private Boolean verifiedOnly;

//    @Column(name = "min_employer_spend")
    private Integer minEmployerSpend;

//    @Column(name = "max_quotes_received")
    private Integer maxQuotesReceived;

//    @Column(name = "not_viewed")
    private Boolean notViewed;

//    @Column(name = "not_applied")
    private Boolean notApplied;

//    @Column(name = "freelancer_id")
    private UUID freelancerId;

}

