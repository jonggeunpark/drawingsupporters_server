package com.drawing.drawing.service;

import com.drawing.drawing.dto.Drawing.DetailDrawingDto;
import com.drawing.drawing.dto.Feedback.DetailFeedbackDto;
import com.drawing.drawing.dto.Feedback.FeedbackAndDrawingInfoDto;
import com.drawing.drawing.dto.Feedback.FeedbackReqeustDto;
import com.drawing.drawing.dto.Feedback.SimpleFeedbackDto;
import com.drawing.drawing.entity.*;
import com.drawing.drawing.exception.NotFoundException;
import com.drawing.drawing.repository.FeedbackRepository;
import com.google.cloud.storage.BlobInfo;
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

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final MenteeService menteeService;
    private final MentorService mentorService;
    private final DrawingService drawingService;
    private final GcsService gcsService;

    public Feedback findById(Long feedbackId) {
        return feedbackRepository.findById(feedbackId).orElseThrow(()-> new NotFoundException("해당 id를 가진 피드백 없음"));
    }

    @Transactional
    public Long saveFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback).getId();
    }

    // 피드백 전체 조회
    public List<SimpleFeedbackDto> readAllFeedback(String storage) {

        List<Feedback> feedbackList = feedbackRepository.findAllByOrderByIdDesc();

        List<SimpleFeedbackDto> feedbackDtoList = new ArrayList<>();

        for(Feedback feedback: feedbackList) {
            feedbackDtoList.add(SimpleFeedbackDto.of(feedback, storage));
        }

        return feedbackDtoList;
    }

    // 피드백 상세 조회
    public FeedbackAndDrawingInfoDto readFeedbackAndDrawing(Long id, String storage) {

        Feedback feedback = feedbackRepository.findById(id).orElseThrow(() -> new NotFoundException("해당 id를 가진 피드백 없음"));
        Drawing drawing = feedback.getDrawing();

        List<URL> urlList = new ArrayList<>();
        for(FeedbackFile feedbackFile: feedback.getFeedbackFileSet()) {
            URL downloadURL = gcsService.generateV4GetObjectSignedUrl(feedbackFile.getUuid() + feedbackFile.getFilename());
            urlList.add(downloadURL);
        }

        return FeedbackAndDrawingInfoDto.of(DetailFeedbackDto.of(feedback, storage), DetailDrawingDto.of(drawing, storage));
    }

    // 피드백 생성
    @Transactional
    public Long createFeedback(MultipartFile file, String email, Long drawingId, FeedbackReqeustDto feedbackReqeustDto) throws IOException {

        Mentor mentor = mentorService.findOneByEmail(email);
        Drawing drawing = drawingService.findById(drawingId);


        Feedback feedback = Feedback.builder()
                .drawing(drawing)
                .mentor(mentor)
                .price(feedbackReqeustDto.getPrice())
                .title(feedbackReqeustDto.getTitle())
                .description(feedbackReqeustDto.getDescription())
                .registrationDate(LocalDate.now())
                .build();

        // 파일명 앞에 랜덤 값 부여
        UUID uuid = UUID.randomUUID();

        // 파일 업로드
        BlobInfo blobInfo = gcsService.uploadFileToGCS(uuid, file);

        FeedbackFile feedbackFile = FeedbackFile.builder()
                .feedback(feedback)
                .filename(file.getOriginalFilename())
                .uuid(uuid.toString())
                .build();

        return saveFeedback(feedback);
    }
}
