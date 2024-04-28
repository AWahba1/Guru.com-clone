package com.guru.freelancerservice.dtos;

import com.guru.freelancerservice.models.Freelancer;
import lombok.*;

import java.util.List;



@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Data
public class FreelancerProfileDto {
    private Freelancer freelancer;
    private List<String> resourceSkills;
    private List<String> serviceSkills;
}
