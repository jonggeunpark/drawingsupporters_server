package com.drawing.drawing.dto.Feedback;

import com.drawing.drawing.entity.Drawing;
import com.drawing.drawing.entity.Feedback;
import com.drawing.drawing.entity.Mento;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FeedbackReqeustDto {

    private Long drawing_id;
    private String title;
    private String description;
    private int price;
    private String feedback_file_type;

    public Feedback toEntity(Mento mento, Drawing drawing, String uuid, String filename) {
        return Feedback.builder()
                .mento(mento)
                .drawing(drawing)
                .title(title)
                .description(description)
                .price(price)
                .feedbackFileType(feedback_file_type)
                .uuid(uuid)
                .filename(filename)
                .registDate(LocalDateTime.now())
                .build();
    }

}
