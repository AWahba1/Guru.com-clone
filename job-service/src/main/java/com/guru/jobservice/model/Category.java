package com.guru.jobservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    public UUID id;
    public String categoryName;
    @JdbcTypeCode(SqlTypes.JSON)
    public List<Subcategory> subcategories;
}


class Subcategory {
    @Id
    public UUID id;
    public String name;
    @JdbcTypeCode(SqlTypes.JSON)
    public List<Skill> skills;
}


