package com.guru.freelancerservice.commands;


import org.springframework.http.ResponseEntity;

public interface Command {
    void execute();
    ResponseEntity<?> getResponse();
}
