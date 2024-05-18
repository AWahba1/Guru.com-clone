package com.messageApp.Models;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;


@Entity
@Table(name = "message")
@IdClass(messagePrimaryKey.class)
@Data
@Builder
//@NoArgsConstructor
public class Message implements Serializable {

   @Id
    UUID conversation_id;

    @Id
    Timestamp sent_at;
    @Id
    UUID message_id;
    private UUID sender_id;
    private UUID receiver_id;
    private String sender_name;
    private String receiver_name;
    private String message_text;

    private String message_file;


    public Message(UUID conversation_id, Timestamp sent_at, UUID message_id, UUID sender_id, UUID receiver_id, String sender_name, String receiver_name, String message_text,String message_file) {
        this.conversation_id = conversation_id;
        this.sent_at = sent_at;
        this.message_id = message_id;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.sender_name = sender_name;
        this.receiver_name = receiver_name;
        this.message_text = message_text;
        this.message_file = message_file;

    }

    public Timestamp getSent_at() {
        return sent_at;
    }

    public void setSent_at(Timestamp sent_at) {
        this.sent_at = sent_at;
    }

    public UUID getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(UUID conversation_id) {
        this.conversation_id = conversation_id;
    }


    public UUID getMessage_id() {
        return message_id;
    }

    public void setMessage_id(UUID message_id) {
        this.message_id = message_id;
    }

    public UUID getSender_id() {
        return sender_id;
    }

    public void setSender_id(UUID sender_id) {
        this.sender_id = sender_id;
    }

    public UUID getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(UUID receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }

    public String getMessage_file() {
        return message_file;
    }

    public void setMessage_file(String message_file) {
        this.message_file = message_file;
    }
}
