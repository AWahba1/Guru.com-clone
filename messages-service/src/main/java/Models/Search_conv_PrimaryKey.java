//package Models;
//
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//
//import javax.persistence.Id;
//import java.io.Serializable;
//import java.sql.Timestamp;
//import java.util.UUID;
//@Data
////@AllArgsConstructor
////@NoArgsConstructor
//@EqualsAndHashCode
//public class Search_conv_PrimaryKey {
//
//    UUID user_id;
//    String user_with_conversation_name;
//
//    Timestamp lastEdited;
//
//    UUID conversation_id;
//
//    public Search_conv_PrimaryKey(UUID user_id, String user_with_conversation_name, Timestamp lastEdited, UUID conversation_id) {
//        this.user_id = user_id;
//        this.user_with_conversation_name = user_with_conversation_name;
//        this.lastEdited = lastEdited;
//        this.conversation_id = conversation_id;
//    }
//
//    public UUID getUser_id() {
//        return user_id;
//    }
//
//    public void setUser_id(UUID user_id) {
//        this.user_id = user_id;
//    }
//
//    public String getUser_with_conversation_name() {
//        return user_with_conversation_name;
//    }
//
//    public void setUser_with_conversation_name(String user_with_conversation_name) {
//        this.user_with_conversation_name = user_with_conversation_name;
//    }
//
//    public Timestamp getLastEdited() {
//        return lastEdited;
//    }
//
//    public void setLastEdited(Timestamp lastEdited) {
//        this.lastEdited = lastEdited;
//    }
//
//    public UUID getConversation_id() {
//        return conversation_id;
//    }
//
//    public void setConversation_id(UUID conversation_id) {
//        this.conversation_id = conversation_id;
//    }
//}
