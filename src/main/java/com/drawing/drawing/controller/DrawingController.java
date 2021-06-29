package com.drawing.drawing.controller;

import com.drawing.drawing.constants.Message;
import com.drawing.drawing.constants.ResponseMessage;
import com.drawing.drawing.constants.StatusCode;
import com.drawing.drawing.dto.Drawing.DetailDrawingDto;
import com.drawing.drawing.dto.Drawing.DrawingRequestDto;
import com.drawing.drawing.dto.Drawing.SimpleDrawingDto;
import com.drawing.drawing.dto.Feedback.DetailFeedbackDto;
import com.drawing.drawing.dto.Feedback.SimpleFeedbackDto;
import com.drawing.drawing.entity.Mentee;
import com.drawing.drawing.entity.User;
import com.drawing.drawing.service.DrawingService;
import com.drawing.drawing.service.MenteeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drawing")
public class DrawingController {

    private final DrawingService drawingService;
    private final MenteeService menteeService;

    // 피드백 요청 생성
    @PostMapping()
    public ResponseEntity<Message> createDrawing(@Valid @RequestBody DrawingRequestDto drawingRequestDto) {
        Authentication user = SecurityContextHolder.getContext().getAuthentication();

        Long id = drawingService.createDrawing(user.getName(), drawingRequestDto);

        Message message = new Message(StatusCode.OK, ResponseMessage.CREATE_DRAWING, id);
        return new ResponseEntity<>(message, HttpStatus.OK);

    }

    // 피드백 요청 전체 조회
    @GetMapping()
    public ResponseEntity<Message> readAllDrawing() {

        Authentication user = SecurityContextHolder.getContext().getAuthentication();

        List<SimpleDrawingDto> response = drawingService.readAllDrawing(user.getName());

        Message message = new Message(StatusCode.OK, ResponseMessage.READ_ALL_DRAWING, response);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // 피드백 요청 상세 조회
    @GetMapping("/{drawingId}")
    private ResponseEntity<Message> readDrawing(@PathVariable("drawingId") Long drawingId) {

        Authentication user = SecurityContextHolder.getContext().getAuthentication();

        DetailDrawingDto response = drawingService.readDrawing(user.getName(), drawingId);

        Message message = new Message(StatusCode.OK, ResponseMessage.READ_DRAWING, response);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
