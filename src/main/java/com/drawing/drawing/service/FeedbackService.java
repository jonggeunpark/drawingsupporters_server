package com.drawing.drawing.service;

import com.drawing.drawing.dto.Drawing.DrawingRequestDto;
import com.drawing.drawing.dto.Feedback.DetailFeedbackDto;
import com.drawing.drawing.dto.Feedback.FeedbackReqeustDto;
import com.drawing.drawing.dto.Feedback.SimpleFeedbackDto;
import com.drawing.drawing.entity.Drawing;
import com.drawing.drawing.entity.Feedback;
import com.drawing.drawing.entity.Mentee;
import com.drawing.drawing.entity.Mento;
import com.drawing.drawing.exception.NotFoundException;
import com.drawing.drawing.repository.FeedbackRepository;
import com.google.cloud.storage.BlobInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final MenteeService menteeService;
    private final MentoService mentoService;
    private final DrawingService drawingService;
    private final GcsService gcsService;

    // 피드백 전체 조회
    public List<SimpleFeedbackDto> readAllFeedback() {
        List<Feedback> feedbackList = feedbackRepository.findAll();

        List<SimpleFeedbackDto> feedbackDtoList = new ArrayList<>();

        for(Feedback feedback: feedbackList) {
            feedbackDtoList.add(SimpleFeedbackDto.of(feedback));
        }

        return feedbackDtoList;
    }

    // 피드백 상세 조회
    public DetailFeedbackDto readFeedback(String email, Long id) {

        Mentee mentee = menteeService.findOneByEmail(email);

        Feedback feedback = feedbackRepository.findById(id).orElseThrow(() -> new NotFoundException("해당 id를 가진 피드백 없음"));

        if(feedback.getDrawing().getMentee() != mentee) {
            throw new NotFoundException("id가 유효하지 않음");
        }

        return DetailFeedbackDto.of(feedback);
    }

    // 피드백 생성
    @Transactional
    public Long createFeedback(MultipartFile file, String email, FeedbackReqeustDto feedbackReqeustDto) throws IOException {

        Mento mento = mentoService.findOneByEmail(email);
        Drawing drawing = drawingService.findById(feedbackReqeustDto.getDrawing_id());

        // 파일명 앞에 랜덤 값 부여
        UUID uuid = UUID.randomUUID();

        // 파일 업로드
        BlobInfo blobInfo = gcsService.uploadFileToGCS(uuid, file);

        // DB에 정보 저장
        Feedback feedback = feedbackReqeustDto.toEntity(mento, drawing, uuid.toString(), file.getOriginalFilename());

        return feedbackRepository.save(feedback).getId();
    }
}
