package com.guru.mediaservice.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class Attachment {

    public String firebaseFileName;
    private String originalFileName;
    private String url;

}
