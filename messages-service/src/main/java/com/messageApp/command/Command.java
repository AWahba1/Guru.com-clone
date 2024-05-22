package com.messageApp.command;

import com.messageApp.Models.Message;
import org.springframework.http.ResponseEntity;

public interface Command {
    void execute();
    ResponseEntity<?> getResponse();
}
