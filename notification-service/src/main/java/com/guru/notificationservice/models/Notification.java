package com.guru.notificationservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(
            strategy = GenerationType.UUID
    )
    private UUID id;
    private UUID user_id;
    private String message;
    private Timestamp created_at;
    private boolean is_read;

    public Notification(UUID user_id, String message, Timestamp created_at, boolean is_read) {
        this.user_id = user_id;
        this.message = message;
        this.created_at = created_at;
        this.is_read = is_read;
    }
//    public Notification(UUID user_id, String message) {
//        this.user_id = user_id;
//        this.message = message;
//    }

}
