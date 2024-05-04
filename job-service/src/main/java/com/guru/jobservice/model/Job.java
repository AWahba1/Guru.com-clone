package com.guru.jobservice.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor


@Entity
@Table(name = "jobs")
public class Job {

    @Id
    public UUID id;
    private String title;
    private String description;
    private UUID categoryId;
    private UUID subcategoryId;
    private boolean featured;
    private UUID clientId;
    private String paymentType;
    private String fixedPriceRange;
    private String duration;
    private String hoursPerWeek;
    private Double minHourlyRate;
    private Double maxHourlyRate;
    private Date getQuotesUntil;
    private String visibility;
    private String status;

    private Date createdAt;
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Skill> skills;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<Timezone> timezones;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<Location> locations;

}

class Skill {
    public String id;
    public String name;
}

class Location {
    public String id;
    public String name;
}

class Timezone {
    public String id;
    public String name;
}

