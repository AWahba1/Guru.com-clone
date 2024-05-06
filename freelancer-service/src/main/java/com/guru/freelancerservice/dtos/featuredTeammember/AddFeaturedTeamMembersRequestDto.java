package com.guru.freelancerservice.dtos.featuredTeammember;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddFeaturedTeamMembersRequestDto {
    /*
     /*
    IN _freelancer_id uuid,
    IN _member_names varchar(255) ARRAY,
    IN _titles varchar(255) ARRAY,
    IN _member_types varchar(255) ARRAY,
    IN _member_emails varchar(255) ARRAY
     */
    private UUID freelancer_id;
    private String[] member_names;
    private String[] titles;
    private String[] member_types;
    private String[] member_emails;
}
