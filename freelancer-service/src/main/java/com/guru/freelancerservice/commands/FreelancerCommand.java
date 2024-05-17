package com.guru.freelancerservice.commands;

import com.guru.freelancerservice.messages.ViewProfileMessageDto;

public interface FreelancerCommand {

    void execute(ViewProfileMessageDto viewProfileMessageDto);
}
