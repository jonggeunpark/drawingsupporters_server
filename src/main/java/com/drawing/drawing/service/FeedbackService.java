package com.drawing.drawing.service;

import com.drawing.drawing.dto.Feedback.DetailFeedbackDto;
import com.drawing.drawing.dto.Feedback.SimpleFeedbackDto;
import com.drawing.drawing.entity.Feedback;
import com.drawing.drawing.exception.NotFoundException;
import com.drawing.drawing.repository.FeedbackRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

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
    public DetailFeedbackDto readFeedback(Long id) {

        return DetailFeedbackDto.of(feedbackRepository.findById(id).orElseThrow(() -> new NotFoundException("id " + id)));
    }
}
