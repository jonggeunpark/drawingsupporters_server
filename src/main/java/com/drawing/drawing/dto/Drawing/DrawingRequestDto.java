package com.drawing.drawing.dto.Drawing;


import com.drawing.drawing.dto.Feedback.PaymentDto;
import com.drawing.drawing.entity.Authority;
import com.drawing.drawing.entity.Drawing;
import com.drawing.drawing.entity.Mentee;
import com.drawing.drawing.entity.Mento;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
public class DrawingRequestDto {

    private String title;
    private String description;
    private int start;
    private int end;
    private PaymentDto payment;
    private List<String> feedback_type;
    private String feedback_file_type;
    private MultipartFile file;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime end_time;
    private String phone_number;


    public Drawing toEntity(Mentee mentee, String uuid, String filename) {

        return Drawing.builder()
                .mentee(mentee)
                .title(title)
                .description(description)
                .feedbackType(String.join(",", feedback_file_type))
                .endDate(end_time)
                .priceLowerLimit(start)
                .priceUpperLimit(end)
                .uuid(uuid)
                .filename(filename)
                .phoneNumber(phone_number)
                .registDate(LocalDateTime.now())
                .build();
    }
}