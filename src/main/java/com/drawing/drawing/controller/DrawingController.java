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
import com.drawing.drawing.exception.UnauthorizedException;
import com.drawing.drawing.service.DrawingService;
import com.drawing.drawing.service.MenteeService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drawing")
public class DrawingController {

    @Value("${storage}")
    private String storage;

    private final DrawingService drawingService;
    private final UserService userService;
    private final MenteeService menteeService;

    /**
     * 피드백 요청 생성
     * METHOD : POST
     * URI : /api/drawing
     * 권한 : 로그인, 학생
     * file과 json을 동시에 받음
     */
    @PostMapping(consumes = {"multipart/form-data"})
    @ResponseBody
    public ResponseEntity<Message> createDrawing(
            @RequestPart("properties") @Valid DrawingRequestDto drawingRequestDto,
            @RequestPart("file") @Valid @NotNull @NotBlank MultipartFile file) throws IOException {

        Authentication user = SecurityContextHolder.getContext().getAuthentication();

        if(!userService.isMentee()) throw new UnauthorizedException(": user type does not match");

        Long id = drawingService.createDrawing(file, user.getName(), drawingRequestDto);

        Message message = new Message(StatusCode.OK, ResponseMessage.CREATE_DRAWING, id);
        return new ResponseEntity<>(message, HttpStatus.OK);

    }

    /**
     * 피드백 요청 전체 조회
     * METHOD : GET
     * URI : /api/drawing
     * 권한 : 로그인, 학생
     */
    @GetMapping()
    public ResponseEntity<Message> readAllDrawing() {

        Authentication user = SecurityContextHolder.getContext().getAuthentication();

        if(!userService.isMentee()) throw new UnauthorizedException(": user type does not match");

        List<SimpleDrawingDto> response = drawingService.readAllDrawing(user.getName(), storage);

        Message message = new Message(StatusCode.OK, ResponseMessage.READ_ALL_DRAWING, response);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * 피드백 요청 상세 조회
     * METHOD : GET
     * URI : /api/drawing/{drawingId}
     * 권한 : 로그인, 학생
     * 해당 drawing이 학생의 drawing 이어야함
     */
    @GetMapping("/{drawingId}")
    private ResponseEntity<Message> readDrawing(@PathVariable("drawingId") Long drawingId) {

        Authentication user = SecurityContextHolder.getContext().getAuthentication();

        if(!userService.isMentee()) throw new UnauthorizedException(": user type does not match");

        DetailDrawingDto response = drawingService.readDrawing(user.getName(), drawingId);

        Message message = new Message(StatusCode.OK, ResponseMessage.READ_DRAWING, response);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
