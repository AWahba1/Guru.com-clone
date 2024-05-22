package com.feedbackApp.Command;

import com.feedbackApp.Controller.FeedbackService;
import com.feedbackApp.DTO.FeedbackDTO;
import com.feedbackApp.DTO.FeedbackEditDTO;
import org.springframework.http.ResponseEntity;

public class EditFeedbackCommand implements Command {
    private final FeedbackService feedbackService;
    private final FeedbackEditDTO feedbackEditDTO;
    private ResponseEntity<?> responseEntity;

    public EditFeedbackCommand(FeedbackService feedbackService, FeedbackEditDTO feedbackEditDTO) {
        this.feedbackService = feedbackService;
        this.feedbackEditDTO = feedbackEditDTO;
    }

    @Override
    public void execute() {
        responseEntity = feedbackService.editFeedback(feedbackEditDTO);
    }

    @Override
    public ResponseEntity<?> getResponse() {
        return responseEntity;
    }
}
