package com.feedbackApp.Command;

import org.springframework.http.ResponseEntity;

public interface Command {
    void execute();
    ResponseEntity<?> getResponse();
}
