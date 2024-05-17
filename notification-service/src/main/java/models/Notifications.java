package models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Entity(name = "notifications")
public class Notifications {
    @Id
    @GeneratedValue(
            strategy = GenerationType.UUID
    )
    private UUID id;
    private UUID user_id;
    private String message;
    private Timestamp created_at;


}
