package com.messageApp.Models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;


@Entity
@Table(name = "see_conversations")
@IdClass(See_conv_PrimaryKey.class)
@Data
public class See_conversations {
    @Id
    UUID user_id;

    @Id
    UUID conversation_id;

    private Timestamp lastEdited;
    private UUID user_with_conversation_id;
    private String user_name;
    private String user_with_conversation_name;
    private Boolean chat_open;

    public See_conversations(UUID user_id, UUID conversation_id,Timestamp lastEdited, UUID user_with_conversation_id, String user_name, String user_with_conversation_name,Boolean chat_open) {
        this.user_id = user_id;
        this.lastEdited = lastEdited;
        this.conversation_id = conversation_id;
        this.user_with_conversation_id = user_with_conversation_id;
        this.user_name = user_name;
        this.user_with_conversation_name = user_with_conversation_name;
        this.chat_open = chat_open;

    }

    public Boolean getChat_open() {
        return chat_open;
    }

    public void setChat_open(Boolean chat_open) {
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

    public String getUser_with_conversation_name() {
        return user_with_conversation_name;
    }

    public void setUser_with_conversation_name(String user_with_conversation_name) {
        this.user_with_conversation_name = user_with_conversation_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }


}
