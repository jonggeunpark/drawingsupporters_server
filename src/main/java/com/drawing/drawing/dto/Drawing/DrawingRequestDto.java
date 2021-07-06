package com.drawing.drawing.dto.Drawing;


import com.drawing.drawing.entity.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
public class DrawingRequestDto {

    private String title;
    private String description;
    private int price_lower_limit;
    private int price_upper_limit;
    private List<String> feedback_type;
    private String end_time;
    private String phone_number;


    public Drawing toEntity(Mentee mentee, String uuid, String filename) {

        LocalDate dateTime = LocalDate.parse(end_time, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return Drawing.builder()
                .mentee(mentee)
                .title(title)
                .description(description)
                .feedbackType(String.join(",", feedback_type))
                .endDate(dateTime)
                .priceLowerLimit(price_lower_limit)
                .priceUpperLimit(price_upper_limit)
                .uuid(uuid)
                .filename(filename)
                .phoneNumber(phone_number)
                .registDate(LocalDate.now())
                .status(DrawingStatus.REQUESTED)
                .build();
    }
}
