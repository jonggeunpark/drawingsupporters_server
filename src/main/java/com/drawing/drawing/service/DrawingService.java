package com.drawing.drawing.service;

import com.drawing.drawing.dto.Drawing.DetailDrawingDto;
import com.drawing.drawing.dto.Drawing.DrawingRequestDto;
import com.drawing.drawing.dto.Drawing.SimpleDrawingDto;
import com.drawing.drawing.dto.Feedback.DetailFeedbackDto;
import com.drawing.drawing.dto.Feedback.SimpleFeedbackDto;
import com.drawing.drawing.entity.Drawing;
import com.drawing.drawing.entity.Feedback;
import com.drawing.drawing.entity.Mentee;
import com.drawing.drawing.exception.NotFoundException;
import com.drawing.drawing.repository.DrawingRepository;
import com.google.cloud.storage.BlobInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class DrawingService {

    private final DrawingRepository drawingRepository;
    private final MenteeService menteeService;
    private final GcsService gcsService;

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

        return drawingRepository.save(drawing).getId();
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

    // 피드백 요청 상세 조회
    public DetailDrawingDto readDrawing(String email, Long id) {

        Mentee mentee = menteeService.findOneByEmail(email);

        Drawing drawing = drawingRepository.findById(id).orElseThrow(() -> new NotFoundException("해당 id를 가진 피드백 요청 없음"));

        if(drawing.getMentee() != mentee) {
            throw new NotFoundException("id가 유효하지 않음");
        }

        return DetailDrawingDto.of(drawing);
    }

}
