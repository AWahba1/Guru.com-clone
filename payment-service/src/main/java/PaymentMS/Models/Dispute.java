package PaymentMS.Models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "disputes")
public class Dispute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Transaction transaction;

    @ManyToOne
    private User initiator;

    private String reason;
    private LocalDateTime timestamp;
    private String status; // OPEN, CLOSED, RESOLVED, ESCALATED

    public Dispute(Long id, Transaction transaction, User initiator, String reason, LocalDateTime timestamp, String status) {
        this.id = id;
        this.transaction = transaction;
        this.initiator = initiator;
        this.reason = reason;
        this.timestamp = timestamp;
        this.status = status;
    }

    public Dispute() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public User getInitiator() {
        return initiator;
    }

    public void setInitiator(User initiator) {
        this.initiator = initiator;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

