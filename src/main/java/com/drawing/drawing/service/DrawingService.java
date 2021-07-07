package com.drawing.drawing.service;

import com.drawing.drawing.dto.Drawing.DetailDrawingDto;
import com.drawing.drawing.dto.Drawing.DrawingRequestDto;
import com.drawing.drawing.dto.Drawing.SimpleDrawingDto;
import com.drawing.drawing.dto.Feedback.SimpleFeedbackDto;
import com.drawing.drawing.entity.*;
import com.drawing.drawing.exception.NotFoundException;
import com.drawing.drawing.exception.UnauthorizedException;
import com.drawing.drawing.repository.DrawingRepository;
import com.drawing.drawing.repository.FeedbackRepository;
import com.google.cloud.storage.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class DrawingService {

    private final DrawingRepository drawingRepository;
    private final FeedbackRepository feedbackRepository;
    private final MenteeService menteeService;
    private final MentoService mentoService;
    private final GcsService gcsService;

    public Drawing findById(Long drawingId) {
        return drawingRepository.findById(drawingId).orElseThrow(()-> new NotFoundException("해당 id를 가진 피드백 요청 없음"));
    }

    @Transactional
    public Long saveDrawing(Drawing drawing) {
        return drawingRepository.save(drawing).getId();
    }

    // 피드백 요청
    @Transactional
    public Long createDrawing(MultipartFile file, String email, DrawingRequestDto drawingRequestDto) throws IOException {

        Mentee mentee = menteeService.findOneByEmail(email);

        // 파일명 앞에 랜덤 값 부여
        UUID uuid = UUID.randomUUID();

        // 파일 업로드
        BlobInfo blobInfo = gcsService.uploadFileToGCS(uuid, file);

        // DB에 정보 저장
        Drawing drawing = drawingRequestDto.toEntity(mentee, uuid.toString(), file.getOriginalFilename());

        return saveDrawing(drawing);
    }

    // 피드백 요청 전체 조회
    public List<SimpleDrawingDto> readAllDrawing(String email, String storage) {

        Mentee mentee = menteeService.findOneByEmail(email);

        List<SimpleDrawingDto> simpleDrawingDtoList = new ArrayList<>();
        for(Drawing drawing : mentee.getDrawingSet()) {
            simpleDrawingDtoList.add(SimpleDrawingDto.of(drawing, storage));
        }

        return simpleDrawingDtoList;
    }

    // 피드백 요청 상세 조회 - 멘티
    public DetailDrawingDto readDrawingByMentee(String email, Long id) {

        Mentee mentee = menteeService.findOneByEmail(email);

        Drawing drawing = findById(id);

        if(drawing.getMentee() != mentee) {
            throw new NotFoundException("id가 유효하지 않음");
        }

        URL url = gcsService.generateV4GetObjectSignedUrl(drawing.getUuid() + drawing.getFilename());
        return DetailDrawingDto.of(drawing, url);
    }

    // 피드백 요청 상세 조회 - 멘토
    public DetailDrawingDto readDrawingByMento(Long drawingId) {

        Drawing drawing = findById(drawingId);

        URL url = gcsService.generateV4GetObjectSignedUrl(drawing.getUuid() + drawing.getFilename());
        return DetailDrawingDto.of(drawing, url);
    }


    // 피드백 요청 접수 ( FeedbackStatus requested -> accepted로 변경, Feedback 생성 )
    @Transactional
    public Long updateDrawingStatus(String email, Long drawingId) {

        Mento mento = mentoService.findOneByEmail(email);
        Drawing drawing = findById(drawingId);

        // 이미 접수되었을 경우
        if(drawing.getFeedback() != null) {
            throw new UnauthorizedException("이미 접수된 피드백 요청입니다.");
        }

        drawing.changeDrawingStatus(DrawingStatus.ACCEPTED);

        Feedback feedback = Feedback.builder()
                .drawing(drawing)
                .mento(mento)

                .acceptDate(LocalDate.now())
                .build();

        saveDrawing(drawing);
        return feedbackRepository.save(feedback).getId();
    }

    // 요청 상태 피드백 요청 목록 조회
    public List<SimpleDrawingDto> readRequestedDrawing(String email, String storage) {

        Mento mento = mentoService.findOneByEmail(email);
        List<Drawing> drawingList = drawingRepository.findAll();

        List<SimpleDrawingDto> simpleDrawingDtoList = new ArrayList<>();

        for(Drawing drawing : drawingList) {
            if(drawing.getDrawingStatus() == DrawingStatus.REQUESTED)
            simpleDrawingDtoList.add(SimpleDrawingDto.of(drawing, storage));
        }

        return simpleDrawingDtoList;
    }

    // 접수 상태 피드백 목록 조회
    public List<SimpleDrawingDto> readAcceptedDrawing(String email, String storage) {
        Mento mento = mentoService.findOneByEmail(email);
        List<SimpleDrawingDto> drawingDtoList = new ArrayList<>();

        for(Feedback feedback: mento.getFeedbackSet()) {
            if(feedback.getStatus() == FeedbackStatus.ACCEPTED){

                drawingDtoList.add(SimpleDrawingDto.of(feedback.getDrawing(), storage));
            }
        }

        return drawingDtoList;
    }
}
