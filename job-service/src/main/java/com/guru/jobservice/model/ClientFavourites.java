package com.guru.jobservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@IdClass(ClientFavourites.ClientFavouriteId.class)
public class ClientFavourites {

    @Id
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
    private User client;

    @Id
    @ManyToOne
    @JoinColumn(name = "freelancer_id", referencedColumnName = "id", nullable = false)
    private Freelancer freelancer;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "added_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date addedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClientFavouriteId implements Serializable {
        private UUID client;
        private UUID freelancer;
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
class User {
    @Id
    private UUID id;
    // other fields and annotations
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "freelancers")
class Freelancer {
    @Id
    private UUID id;
    // other fields and annotations
}
