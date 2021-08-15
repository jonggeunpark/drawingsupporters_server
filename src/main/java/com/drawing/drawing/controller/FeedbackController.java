package com.drawing.drawing.controller;

import com.drawing.drawing.constants.Message;
import com.drawing.drawing.constants.ResponseMessage;
import com.drawing.drawing.constants.StatusCode;
import com.drawing.drawing.dto.Drawing.DrawingRequestDto;
import com.drawing.drawing.dto.Feedback.DetailFeedbackDto;
import com.drawing.drawing.dto.Feedback.FeedbackReqeustDto;
import com.drawing.drawing.dto.Feedback.SimpleFeedbackDto;
import com.drawing.drawing.entity.Feedback;
import com.drawing.drawing.exception.UnauthorizedException;
import com.drawing.drawing.service.FeedbackService;
import com.drawing.drawing.service.MailService;
import com.drawing.drawing.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    @Value("${storage}")
    private String storage;

    private final UserService userService;
    private final FeedbackService feedbackService;
    private final MailService mailService;

    /**
     * 피드백 전체 조회
     * METHOD : GET
     * URI : /api/feedback
     * 권한 : 없음
     */
    @GetMapping()
    private ResponseEntity<Message> readAllFeedback() {

        List<SimpleFeedbackDto> response = feedbackService.readAllFeedback(storage);

        Message message = new Message(StatusCode.OK, ResponseMessage.READ_ALL_FEEDBACK, response);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * 피드백 상세 조회
     * METHOD : GET
     * URI : /api/feedback/{feedbackId}
     * 권한 : 없음
     */
    @GetMapping("/{feedbackId}")
    private ResponseEntity<Message> readFeedback(@PathVariable("feedbackId") Long feedbackId) {

        DetailFeedbackDto response = feedbackService.readFeedback(feedbackId, storage);

        Message message = new Message(StatusCode.OK, ResponseMessage.READ_FEEDBACK, response);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * 피드백 생성
     * METHOD : POST
     * URI : /api/feedback/{feedbackId}
     * 권한 : 로그인, 전문가
     */
    @PostMapping(value = "/{feedbackId}", consumes = {"multipart/form-data"})
    private ResponseEntity<Message> createFeedback(@PathVariable("feedbackId") Long feedbackId,
                                                   @RequestPart("properties") FeedbackReqeustDto feedbackReqeustDto,
                                                   @RequestPart("file") MultipartFile file ) throws Exception {

        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        if(!userService.isMentor()) throw new UnauthorizedException(": user type does not match");

        // 피드백 생성
        Long id = feedbackService.createFeedback(file, user.getName(), feedbackId, feedbackReqeustDto);

        // 메일 전송
        Feedback feedback = feedbackService.findById(feedbackId);
        mailService.sendMail(feedback);

        Message message = new Message(StatusCode.OK, ResponseMessage.CREATE_FEEDBACK, id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }


    /**
     * 접수 상태 피드백 목록 조회
     * METHOD : GET
     * URI : /api/feedback/accepted
     * 권한 : 로그인, 전문가
     */
    @CrossOrigin
    @GetMapping("/accepted")
    private ResponseEntity<Message> readAcceptedFeedback() {

        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        if(!userService.isMentor()) throw new UnauthorizedException(": user type does not match");

        List<SimpleFeedbackDto> response = feedbackService.readAcceptedFeedback(user.getName(), storage);

        Message message = new Message(StatusCode.OK, ResponseMessage.READ_ACCEPTED_FEEDBACK, response);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * 완료 상태 피드백 목록 조회
     * METHOD : GET
     * URI : /api/feedback/completed
     * 권한 : 로그인, 전문가
     */
    @GetMapping("/completed")
    private ResponseEntity<Message> readCompletedFeedback() {

        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        if(!userService.isMentor()) throw new UnauthorizedException(": user type does not match");

        List<SimpleFeedbackDto> response = feedbackService.readCompletedFeedback(user.getName(), storage);

        Message message = new Message(StatusCode.OK, ResponseMessage.READ_COMPLETED_FEEDBACK, response);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
