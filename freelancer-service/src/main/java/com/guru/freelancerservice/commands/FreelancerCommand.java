package com.guru.freelancerservice.commands;

import com.guru.freelancerservice.messages.MessageDto;

import java.util.Optional;

public interface FreelancerCommand {

    void execute(MessageDto messageDto);
}
