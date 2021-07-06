package com.drawing.drawing.dto.Drawing;

import com.drawing.drawing.dto.Feedback.DetailFeedbackDto;
import com.drawing.drawing.dto.Feedback.PaymentDto;
import com.drawing.drawing.entity.Drawing;
import com.drawing.drawing.entity.Feedback;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public class DetailDrawingDto {
    private String title;
    private String description;
    private PaymentDto payment;
    private List<String> feedback_type;
    private String feedback_file_type;
    // private MultipartFile file;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime end_time;

    //multipartfile 수정
    public static DetailDrawingDto of(Drawing drawing) {

        PaymentDto payment = PaymentDto.of(drawing.getPriceLowerLimit(), drawing.getPriceUpperLimit());

        return new DetailDrawingDto(drawing.getTitle(), drawing.getDescription(), payment,
                Arrays.asList(drawing.getFeedbackType().split(",")), drawing.getFeedbackType(), drawing.getRegistDate());
    }
}
