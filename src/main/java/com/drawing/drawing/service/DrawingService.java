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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class DrawingService {

    private final DrawingRepository drawingRepository;
    private final MenteeService menteeService;

    // 피드백 요청
    @Transactional
    public Long createDrawing(String email, DrawingRequestDto drawingRequestDto) {

        Mentee mentee = menteeService.findOneByEmail(email);
        Drawing drawing = drawingRequestDto.toEntity(mentee);

        return drawingRepository.save(drawing).getId();
    }

    // 피드백 요청 전체 조회
    public List<SimpleDrawingDto> readAllDrawing(String email) {

        Mentee mentee = menteeService.findOneByEmail(email);

        List<SimpleDrawingDto> simpleDrawingDtoList = new ArrayList<>();
        for(Drawing drawing : mentee.getDrawingSet()) {
            simpleDrawingDtoList.add(SimpleDrawingDto.of(drawing));
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
