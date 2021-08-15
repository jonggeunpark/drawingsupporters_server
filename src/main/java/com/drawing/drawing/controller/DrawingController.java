package com.drawing.drawing.controller;

import com.drawing.drawing.constants.Message;
import com.drawing.drawing.constants.ResponseMessage;
import com.drawing.drawing.constants.StatusCode;
import com.drawing.drawing.dto.Drawing.DetailDrawingDto;
import com.drawing.drawing.dto.Drawing.DrawingRequestDto;
import com.drawing.drawing.dto.Drawing.SimpleDrawingDto;
import com.drawing.drawing.dto.Feedback.SimpleFeedbackDto;
import com.drawing.drawing.exception.UnauthorizedException;
import com.drawing.drawing.service.DrawingService;
import com.drawing.drawing.service.FeedbackService;
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

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drawing")
public class DrawingController {

    @Value("${storage}")
    private String storage;

    private final DrawingService drawingService;
    private final UserService userService;
    private final FeedbackService feedbackService;
    private final MenteeService menteeService;

    /**
     * 피드백 요청 생성
     * METHOD : POST
     * URI : /api/drawing
     * 권한 : 로그인, 학생
     * file과 json을 동시에 받음
     */
    @CrossOrigin
    @PostMapping(consumes = {"multipart/form-data"})
    @ResponseBody
    public ResponseEntity<Message> createDrawing(
            @RequestPart("properties") @Valid DrawingRequestDto drawingRequestDto,
            @RequestPart(value = "file") @Valid @NotNull @NotBlank List<MultipartFile> files) throws IOException {

        Authentication user = SecurityContextHolder.getContext().getAuthentication();

        if(!userService.isMentee()) throw new UnauthorizedException(": user type does not match");

        Long id = drawingService.createDrawing(files, user.getName(), drawingRequestDto);

        Message message = new Message(StatusCode.OK, ResponseMessage.CREATE_DRAWING, id);
        return new ResponseEntity<>(message, HttpStatus.OK);

    }

    /**
     * 피드백 요청 전체 조회
     * METHOD : GET
     * URI : /api/drawing
     * ////권한 : 로그인, 학생
     * 권한 삭제 - 2021-08-15
     */
    @CrossOrigin
    @GetMapping()
    public ResponseEntity<Message> readAllDrawing() {

        /*
        Authentication user = SecurityContextHolder.getContext().getAuthentication();

        if(!userService.isMentee()) throw new UnauthorizedException(": user type does not match");

        List<SimpleDrawingDto> response = drawingService.readAllDrawing(user.getName(), storage);
         */

        List<SimpleDrawingDto> response = drawingService.readAllDrawing(storage);

        Message message = new Message(StatusCode.OK, ResponseMessage.READ_ALL_DRAWING, response);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * 피드백 요청 상세 조회
     * METHOD : GET
     * URI : /api/drawing/{drawingId}
     * ////권한 : 로그인, 학생 or 전문가
     * ////학생일 경우 : 해당 drawing이 학생의 drawing 이어야함
     * ////전문가일 경우 : 조건 없음
     * 권한 삭제 - 2021-08-15
     */
    @CrossOrigin
    @GetMapping("/{drawingId}")
    private ResponseEntity<Message> readDrawing(@PathVariable("drawingId") Long drawingId) {


        //Authentication user = SecurityContextHolder.getContext().getAuthentication();

        DetailDrawingDto response = null;

        /*
        // 학생일 경우
        if(userService.isMentee()) {
            response = drawingService.readDrawingByMentee(user.getName(), drawingId);
        } else if (userService.isMentor()) {
            response = drawingService.readDrawingByMentor(drawingId);
        } else {
            throw new UnauthorizedException(": user type does not match");
        }
         */
        response = drawingService.readDrawingDetail(drawingId, storage);

        Message message = new Message(StatusCode.OK, ResponseMessage.READ_DRAWING, response);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * 피드백 요청 접수 ( FeedbackStatus requested -> accepted로 변경, Feedback 생성 )
     * METHOD : POST
     * URI : /api/drawing/{drawingId}/status
     * 권한 : 로그인, 전문가
     */
    @CrossOrigin
    @PostMapping("/{drawingId}/status")
    private ResponseEntity<Message> updateDrawingStatus(@PathVariable("drawingId") Long drawingId) {

        Authentication user = SecurityContextHolder.getContext().getAuthentication();

        if(!userService.isMentor()) throw new UnauthorizedException(": user type does not match");

        Long feedback_id = drawingService.updateDrawingStatus(user.getName(), drawingId);

        Message message = new Message(StatusCode.OK, ResponseMessage.UPDATE_DRAWING_STATUS_ACCEPTED, feedback_id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * 요청 상태 피드백 요청 목록 조회
     * METHOD : GET
     * URI : /api/drawing/requested
     * 권한 : 로그인, 전문가
     */
    @CrossOrigin
    @GetMapping("/requested")
    private ResponseEntity<Message> readRequestedDrawing() {

        Authentication user = SecurityContextHolder.getContext().getAuthentication();

        if(!userService.isMentor()) throw new UnauthorizedException(": user type does not match");

        List<SimpleDrawingDto> response = drawingService.readRequestedDrawing(user.getName(), storage);

        Message message = new Message(StatusCode.OK, ResponseMessage.READ_REQUESTED_DRAWING, response);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * 피드백 요청 삭제
     * METHOD : DELETE
     * URI : /api/drawing/{drawingId}
     * 권한 : 로그인, 학생
     */
    @CrossOrigin
    @DeleteMapping("/{drawingId}")
    private ResponseEntity<Message> deleteDrawing(@PathVariable("drawingId") Long drawingId) {

        Authentication user = SecurityContextHolder.getContext().getAuthentication();

        if(!userService.isMentee()) throw new UnauthorizedException(": user type does not match");

        drawingService.deleteDrawing(user.getName(), drawingId);

        Message message = new Message(StatusCode.OK, ResponseMessage.DELETE_DRAWING);
        return new ResponseEntity<>(message, HttpStatus.OK);

    }
}
