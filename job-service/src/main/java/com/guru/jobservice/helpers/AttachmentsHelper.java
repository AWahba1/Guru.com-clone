package com.guru.jobservice.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guru.jobservice.model.Attachment;

public class AttachmentsHelper {

    public static String[] convertAttachmentsToJson(Attachment[] attachments) throws JsonProcessingException {

        if (attachments == null || attachments.length==0) return null;
        ObjectMapper mapper = new ObjectMapper();
        String[] attachmentJsonArray = new String[attachments.length];
        for (int i = 0; i < attachments.length; i++) {
            String attachmentJson = mapper.writeValueAsString(attachments[i]);
            attachmentJsonArray[i] = attachmentJson;
        }
        return attachmentJsonArray;
    }
}
