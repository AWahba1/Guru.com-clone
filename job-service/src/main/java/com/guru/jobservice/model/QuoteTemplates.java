package com.guru.jobservice.model;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;


//id UUID PRIMARY KEY,
//freelancer_id UUID,
//template_name VARCHAR(255),
//template_description VARCHAR(10000),
//attachments VARCHAR(255) ARRAY,
@Entity
@Table(name = "quote_templates")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuoteTemplates {

    @Id
    @GeneratedValue(
            strategy = GenerationType.UUID
    )
    private UUID id;
    private UUID freelancer_id;
    private String template_name;
    private String template_description;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<Attachment> attachments;

}
