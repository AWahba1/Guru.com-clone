package com.guru.freelancerservice.commands.team_commands;
import com.guru.freelancerservice.dtos.featuredTeammember.AddFeaturedTeamMembersRequestDto;
import com.guru.freelancerservice.services.FreelancerService;
import org.springframework.http.ResponseEntity;
import java.util.UUID;

public class AddTeamMemberCommand {

    private final FreelancerService freelancerService;
    private final AddFeaturedTeamMembersRequestDto addFeaturedTeamMembersRequestDto;
    private ResponseEntity<?> responseEntity;

    public AddTeamMemberCommand(FreelancerService freelancerService, AddFeaturedTeamMembersRequestDto addFeaturedTeamMembersRequestDto) {
        this.freelancerService = freelancerService;
        this.addFeaturedTeamMembersRequestDto = addFeaturedTeamMembersRequestDto;
    }

    public void execute() {
        responseEntity = freelancerService.addFeaturedTeamMembers(addFeaturedTeamMembersRequestDto);
    }

    public ResponseEntity<?> getResponse() {
        return responseEntity;
    }

}
