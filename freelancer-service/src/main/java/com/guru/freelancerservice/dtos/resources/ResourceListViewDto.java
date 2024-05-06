package com.guru.freelancerservice.dtos.resources;

//service_id uuid,
//service_title varchar(255),
//service_description varchar(5000),
//service_skills varchar(255) ARRAY,
//service_rate decimal,
//minimum_budget decimal,
//service_thumbnail varchar(255)

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.util.UUID;

//resource_id uuid,
//resource_name varchar(255),
//resource_title varchar(255),
//resource_summary varchar(3000),
//resource_skills varchar(255) ARRAY,
//resource_rate decimal,
//minimum_duration varchar(255),
//resource_image varchar(255)

@JsonPropertyOrder({
        "resource_id", "resource_name", "resource_title", "resource_summary", "resource_skills", "resource_rate", "minimum_duration", "resource_image"
})
public interface ResourceListViewDto {
    UUID getResource_id();
    String getResource_name();
    String getResource_title();
    String getResource_summary();
    String[] getResource_skills();
    BigDecimal getResource_rate();
    String getMinimum_duration();
    String getResource_image();


}
