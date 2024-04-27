package com.guru.freelancerservice.dtos;

import com.guru.freelancerservice.models.Freelancer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class FreelancerProfileDto {
    private Freelancer freelancer;
    private List<ResourceSkillDto> resourceSkills;
    private List<ServiceSkillDto> serviceSkills;


    public Freelancer getFreelancer() {
        return freelancer;
    }

    public void setFreelancer(Freelancer freelancer) {
        this.freelancer = freelancer;
    }

    public List<ResourceSkillDto> getResourceSkills() {
        return resourceSkills;
    }

    public void setResourceSkills(List<ResourceSkillDto> resourceSkills) {
        this.resourceSkills = resourceSkills;
    }

    public List<ServiceSkillDto> getServiceSkills() {
        return serviceSkills;
    }

    public void setServiceSkills(List<ServiceSkillDto> serviceSkills) {
        this.serviceSkills = serviceSkills;
    }


}
