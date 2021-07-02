package com.drawing.drawing.controller;

import com.drawing.drawing.constants.Message;
import com.drawing.drawing.constants.ResponseMessage;
import com.drawing.drawing.constants.StatusCode;
import com.drawing.drawing.dto.Feedback.DetailFeedbackDto;
import com.drawing.drawing.dto.Feedback.SimpleFeedbackDto;
import com.drawing.drawing.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    //피드백 전체 조회
    @GetMapping()
    private ResponseEntity<Message> readAllFeedback() {

        List<SimpleFeedbackDto> response = feedbackService.readAllFeedback();

        Message message = new Message(StatusCode.OK, ResponseMessage.READ_ALL_FEEDBACK, response);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    //피드백 상세 조회
    @GetMapping("/{feedbackId}")
    private ResponseEntity<Message> readFeedback(@PathVariable("feedbackId") Long feedbackId) {

        Authentication user = SecurityContextHolder.getContext().getAuthentication();

        DetailFeedbackDto response = feedbackService.readFeedback(user.getName(), feedbackId);

        Message message = new Message(StatusCode.OK, ResponseMessage.READ_FEEDBACK, response);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}
