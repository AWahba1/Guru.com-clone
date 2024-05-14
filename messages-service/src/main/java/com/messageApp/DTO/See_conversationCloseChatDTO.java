package com.messageApp.DTO;

import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;
import java.util.UUID;

public class See_conversationCloseChatDTO {

    @NotNull
    private UUID user_id;
    @NotNull
    private UUID conversation_id;
    @NotNull
    private Boolean chat_open;

    public See_conversationCloseChatDTO(UUID user_id, UUID conversation_id, Boolean chat_open) {
        this.user_id = user_id;
        this.conversation_id = conversation_id;
        this.chat_open = chat_open;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }

    public UUID getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(UUID conversation_id) {
        this.conversation_id = conversation_id;
    }

    public Boolean getChat_open() {
        return chat_open;
    }

    public void setChat_open(Boolean chat_open) {
        this.chat_open = chat_open;
    }
}
