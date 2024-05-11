package com.messageApp.Models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;
@Data
//@AllArgsConstructor
//@NoArgsConstructor
@EqualsAndHashCode
public class messagePrimaryKey implements Serializable {
    UUID conversation_id;
    Timestamp sent_at;
    UUID message_id;

    public messagePrimaryKey(UUID conversation_id,Timestamp sent_at, UUID message_id) {
        this.conversation_id = conversation_id;
        this.sent_at =   sent_at;
        this.message_id = message_id;

    }

    public UUID getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(UUID conversation_id) {
        this.conversation_id = conversation_id;
    }

    public Timestamp getSent_at() {
        return sent_at;
    }

    public void setSent_at(Timestamp sent_at) {
        this.sent_at = sent_at;
    }

    public UUID getMessage_id() {
        return message_id;
    }

    public void setMessage_id(UUID message_id) {
        this.message_id = message_id;
    }
}
