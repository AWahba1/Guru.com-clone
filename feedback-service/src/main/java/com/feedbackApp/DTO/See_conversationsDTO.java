package com.feedbackApp.DTO;

import com.feedbackApp.Models.See_conversations;

import java.sql.Timestamp;
import java.util.UUID;

public class See_conversationsDTO {
    private UUID user_id;
    private Timestamp lastEdited;
    private UUID conversation_id;

    private UUID user_with_conversation_id;
    private String user_name;
    private String user_with_conversation_name;
    private Boolean chat_open;

    public See_conversationsDTO(UUID user_id, Timestamp lastEdited, UUID conversation_id, UUID user_with_conversation_id, String user_name, String user_with_conversation_name, Boolean chat_open) {
        this.user_id = user_id;
        this.lastEdited = lastEdited;
        this.conversation_id = conversation_id;
        this.user_with_conversation_id = user_with_conversation_id;
        this.user_name = user_name;
        this.user_with_conversation_name = user_with_conversation_name;
        this.chat_open = chat_open;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }

    public Timestamp getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(Timestamp lastEdited) {
        this.lastEdited = lastEdited;
    }

    public UUID getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(UUID conversation_id) {
        this.conversation_id = conversation_id;
    }

    public UUID getUser_with_conversation_id() {
        return user_with_conversation_id;
    }

    public void setUser_with_conversation_id(UUID user_with_conversation_id) {
        this.user_with_conversation_id = user_with_conversation_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_with_conversation_name() {
        return user_with_conversation_name;
    }

    public void setUser_with_conversation_name(String user_with_conversation_name) {
        this.user_with_conversation_name = user_with_conversation_name;
    }

    public Boolean getChat_open() {
        return chat_open;
    }

    public void setChat_open(Boolean chat_open) {
        this.chat_open = chat_open;
    }

    public static See_conversations buildSee_converstions(See_conversationsDTO seeConversationsDTO){
        See_conversations seeConversations = new See_conversations(
                seeConversationsDTO.getUser_id(),
                UUID.randomUUID(),
                seeConversationsDTO.getLastEdited(),
                seeConversationsDTO.getUser_with_conversation_id(),
                seeConversationsDTO.getUser_name(),
                seeConversationsDTO.getUser_with_conversation_name(),
                seeConversationsDTO.chat_open
        );
        return seeConversations;

    }
}
