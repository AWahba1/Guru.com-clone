package com.guru.freelancerservice.dtos;
import lombok.*;

import java.util.UUID;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceSkillDto {
    private UUID skillId;
    private String skillName;

}
