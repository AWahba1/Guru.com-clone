package com.messageApp.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewMessageSentDTO {
    private UUID userId;
    private UUID senderId;
    private String senderName;
}
