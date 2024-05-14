package com.messageApp.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;
import java.util.UUID;

public class UpdateDTO {

    @NotNull
    private UUID conversation_id;

    @NotNull
    private Timestamp sent_at;
    @NotNull
    private UUID message_id;
    @NotNull
    @NotBlank(message = "message_text is mandatory")
    private String message_text;

    public UpdateDTO(UUID conversation_id, Timestamp sent_at, UUID message_id, String message_text) {
        this.conversation_id = conversation_id;
        this.sent_at = sent_at;
        this.message_id = message_id;
        this.message_text = message_text;
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

    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }
}
