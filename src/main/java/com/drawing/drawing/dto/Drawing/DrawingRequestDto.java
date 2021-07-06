package com.drawing.drawing.dto.Drawing;


import com.drawing.drawing.dto.Feedback.PaymentDto;
import com.drawing.drawing.entity.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Getter
public class DrawingRequestDto {

    private String title;
    private String description;
    private int start;
    private int end;
    private List<String> feedback_type;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private String end_time;
    private String phone_number;


    public Drawing toEntity(Mentee mentee, String uuid, String filename) {

        LocalDateTime dateTime = LocalDateTime.parse(end_time, DateTimeFormatter.ISO_DATE);

        return Drawing.builder()
                .mentee(mentee)
                .title(title)
                .description(description)
                .feedbackType(String.join(",", feedback_type))
                .endDate(dateTime)
                .priceLowerLimit(start)
                .priceUpperLimit(end)
                .uuid(uuid)
                .filename(filename)
                .phoneNumber(phone_number)
                .registDate(LocalDateTime.now())
                .status(DrawingStatus.REQUESTED)
                .build();
    }
}
