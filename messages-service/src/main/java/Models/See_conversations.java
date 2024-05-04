package Models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;


@Entity
@Table(name = "message")
@IdClass(messagePrimaryKey.class)
@Data

public class See_conversations {
    @Id
    UUID user_id;
    @Id
    Timestamp lastEdited;
    @Id
    UUID conversation_id;

    private UUID user_id_with_conversation;
    private String user_name;
    private String user_id_with_conversation_name;

    public See_conversations(UUID user_id, Timestamp lastEdited, UUID conversation_id, UUID user_id_with_conversation, String user_name, String user_id_with_conversation_name) {
        this.user_id = user_id;
        this.lastEdited = lastEdited;
        this.conversation_id = conversation_id;
        this.user_id_with_conversation = user_id_with_conversation;
        this.user_name = user_name;
        this.user_id_with_conversation_name = user_id_with_conversation_name;
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

    public UUID getUser_id_with_conversation() {
        return user_id_with_conversation;
    }

    public void setUser_id_with_conversation(UUID user_id_with_conversation) {
        this.user_id_with_conversation = user_id_with_conversation;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_id_with_conversation_name() {
        return user_id_with_conversation_name;
    }

    public void setUser_id_with_conversation_name(String user_id_with_conversation_name) {
        this.user_id_with_conversation_name = user_id_with_conversation_name;
    }
}
