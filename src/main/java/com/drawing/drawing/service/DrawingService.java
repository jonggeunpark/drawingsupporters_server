package com.drawing.drawing.service;

import com.drawing.drawing.dto.Drawing.DetailDrawingDto;
import com.drawing.drawing.dto.Drawing.DrawingAndFeedbackListInfo;
import com.drawing.drawing.dto.Drawing.DrawingRequestDto;
import com.drawing.drawing.dto.Drawing.SimpleDrawingDto;
import com.drawing.drawing.dto.Feedback.DetailFeedbackDto;
import com.drawing.drawing.entity.*;
import com.drawing.drawing.exception.NotFoundException;
import com.drawing.drawing.repository.DrawingRepository;
import com.drawing.drawing.repository.FeedbackRepository;
import com.google.cloud.storage.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class DrawingService {

    private final DrawingRepository drawingRepository;
    private final FeedbackRepository feedbackRepository;
    private final MenteeService menteeService;
    private final MentorService mentorService;
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
    public Long createDrawing(List<MultipartFile> files, String email, DrawingRequestDto drawingRequestDto) throws IOException {

        Mentee mentee = menteeService.findOneByEmail(email);

        // Drawing 생성
        Drawing drawing = drawingRequestDto.toEntity(mentee);

        for(MultipartFile file: files) {
            // 파일명 앞에 랜덤 값 부여
            UUID uuid = UUID.randomUUID();

            // 파일 업로드
            BlobInfo blobInfo = gcsService.uploadFileToGCS(uuid, file);

            // drawingFile 생성
            DrawingFile drawingFile = DrawingFile.builder()
                    .drawing(drawing)
                    .uuid(uuid.toString())
                    .filename(file.getOriginalFilename())
                    .build();
        }

        // 저장
        return saveDrawing(drawing);
    }

    /*
    // 피드백 요청 전체 조회
    public List<SimpleDrawingDto> readAllDrawing(String email, String storage) {

        Mentee mentee = menteeService.findOneByEmail(email);

        List<SimpleDrawingDto> simpleDrawingDtoList = new ArrayList<>();
        for(Drawing drawing : mentee.getDrawingSet()) {
            simpleDrawingDtoList.add(SimpleDrawingDto.of(drawing, storage));
        }

        return simpleDrawingDtoList;
    }

     */
    public List<SimpleDrawingDto> readAllDrawing(String storage) {

        List<Drawing> drawingList = drawingRepository.findAllByOrderByIdDesc();

        List<SimpleDrawingDto> simpleDrawingDtoList = new ArrayList<>();
        for(Drawing drawing : drawingList) {
            simpleDrawingDtoList.add(SimpleDrawingDto.of(drawing, storage));
        }

        return simpleDrawingDtoList;
    }

    /*
    // 피드백 요청 상세 조회 - 멘티
    public DetailDrawingDto readDrawingByMentee(String email, Long id) {

        Mentee mentee = menteeService.findOneByEmail(email);

        Drawing drawing = findById(id);

        if(drawing.getMentee() != mentee) {
            throw new NotFoundException("id가 유효하지 않음");
        }

        List<URL> urlList = new ArrayList<>();
        for (DrawingFile drawingFile : drawing.getDrawingFileSet()) {
            URL url = gcsService.generateV4GetObjectSignedUrl(drawingFile.getUuid() + drawingFile.getFilename());
            urlList.add(url);
        }

        return DetailDrawingDto.of(drawing, urlList);
    }

    // 피드백 요청 상세 조회 - 멘토
    public DetailDrawingDto readDrawingByMentor(Long drawingId) {

        Drawing drawing = findById(drawingId);

        List<URL> urlList = new ArrayList<>();
        for (DrawingFile drawingFile : drawing.getDrawingFileSet()) {
            URL url = gcsService.generateV4GetObjectSignedUrl(drawingFile.getUuid() + drawingFile.getFilename());
            urlList.add(url);
        }

        return DetailDrawingDto.of(drawing, urlList);
    }
     */

    // 피드백 요청 상세 조회 - 권한 X
    public DrawingAndFeedbackListInfo readDrawingAndFeedbackList(Long drawingId, String storage) {

        Drawing drawing = findById(drawingId);

        List<Feedback> feedbackList = feedbackRepository.findAllByDrawingIdOrderById(drawingId);
        List<DetailFeedbackDto> feedbackDtoList = new ArrayList<>();

        for(Feedback feedback: feedbackList) {
            DetailFeedbackDto detailFeedbackDto = DetailFeedbackDto.of(feedback,storage);
            feedbackDtoList.add(detailFeedbackDto);
        }
        return DrawingAndFeedbackListInfo.of(DetailDrawingDto.of(drawing, storage), feedbackDtoList);
    }

    // 요청 상태 피드백 요청 목록 조회
    public List<SimpleDrawingDto> readRequestedDrawing(String email, String storage) {

        Mentor mentor = mentorService.findOneByEmail(email);

        List<Drawing> drawingList = drawingRepository.findAllByDrawingStatusOrderByIdDesc(DrawingStatus.REQUESTED);

        List<SimpleDrawingDto> simpleDrawingDtoList = new ArrayList<>();

        for(Drawing drawing : drawingList) {
            simpleDrawingDtoList.add(SimpleDrawingDto.of(drawing, storage));
        }

        return simpleDrawingDtoList;
    }


    @Transactional
    public void deleteDrawing(String email, Long drawingId) {

        Mentee mentee = menteeService.findOneByEmail(email);

        Drawing drawing = drawingRepository.findById(drawingId).orElseThrow(() -> new NotFoundException("해당 id를 가진 피드백 요청 없음"));

        if(drawing.getMentee() != mentee) {
            throw new NotFoundException("id가 유효하지 않음");
        }

        drawingRepository.deleteById(drawingId);
    }
}
