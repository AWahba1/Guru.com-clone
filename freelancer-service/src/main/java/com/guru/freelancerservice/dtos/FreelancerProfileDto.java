package com.guru.freelancerservice.dtos;

import com.guru.freelancerservice.models.Freelancer;
import lombok.*;

import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FreelancerProfileDto {
    private Freelancer freelancer;
    private List<ResourceSkillDto> resourceSkills;
    private List<ResourceSkillDto> serviceSkills;

}
