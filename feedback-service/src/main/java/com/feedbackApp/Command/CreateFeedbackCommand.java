package com.feedbackApp.Command;

import com.feedbackApp.Controller.FeedbackService;
import com.feedbackApp.DTO.FeedbackDTO;
import org.springframework.http.ResponseEntity;

public class CreateFeedbackCommand implements Command {
    private final FeedbackService feedbackService;
    private final FeedbackDTO feedbackDTO;
    private ResponseEntity<?> responseEntity;

    public CreateFeedbackCommand(FeedbackService feedbackService, FeedbackDTO feedbackDTO) {
        this.feedbackService = feedbackService;
        this.feedbackDTO = feedbackDTO;
    }

    @Override
    public void execute() {
        responseEntity = feedbackService.createFeedback(feedbackDTO);
    }

    @Override
    public ResponseEntity<?> getResponse() {
        return responseEntity;
    }
}
