package com.messageApp.Models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;
@Data
//@AllArgsConstructor
//@NoArgsConstructor
@EqualsAndHashCode
public class See_conv_PrimaryKey implements Serializable {

    UUID user_id;

    UUID conversation_id;

    public See_conv_PrimaryKey(UUID user_id, UUID conversation_id) {
        this.user_id = user_id;
        this.conversation_id = conversation_id;
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
}
