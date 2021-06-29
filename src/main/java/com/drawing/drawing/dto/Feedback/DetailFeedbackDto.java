package com.drawing.drawing.dto.Feedback;

import com.drawing.drawing.entity.Drawing;
import com.drawing.drawing.entity.Feedback;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public class DetailFeedbackDto {

    private String title;
    private String description;
    private PaymentDto payment;
    private List<String> feedback_type;
    private String feedback_file_type;
    private String file;

    @JsonFormat(pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDateTime end_time;

    public static DetailFeedbackDto of(Feedback feedback) {
        Drawing drawing = feedback.getDrawing();
        PaymentDto payment = PaymentDto.of(drawing.getPriceLowerLimit(), drawing.getPriceUpperLimit());
        return new DetailFeedbackDto(feedback.getTitle(), feedback.getDescription(), payment,
                Arrays.asList(drawing.getFeedbackType().split(",")), feedback.getFeedbackType(), feedback.getFile(), feedback.getRegistDate());
    }

}
