package com.drawing.drawing.dto.Drawing;


import com.drawing.drawing.entity.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;

@Getter
public class DrawingRequestDto {

    private String title;
    private String description;
    private int price_lower_limit;
    private int price_upper_limit;
    private List<String> feedback_type;
    private String deadline;
    private String phone_number;


    public Drawing toEntity(Mentee mentee) {

        LocalDate dateTime = LocalDate.parse(deadline, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return Drawing.builder()
                .mentee(mentee)
                .title(title)
                .description(description)
                .feedbackType(String.join(",", feedback_type))
                .deadline(dateTime)
                .priceLowerLimit(price_lower_limit)
                .priceUpperLimit(price_upper_limit)
                .phoneNumber(phone_number)
                .registrationDate(LocalDate.now())
                .drawingStatus(DrawingStatus.REQUESTED)
                .drawingFileSet(new HashSet<>())
                .feedbackSet(new HashSet<>())
                .build();
    }
}
