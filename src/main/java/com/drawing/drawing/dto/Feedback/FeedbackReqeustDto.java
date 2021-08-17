package com.drawing.drawing.dto.Feedback;

import com.drawing.drawing.entity.Drawing;
import com.drawing.drawing.entity.Feedback;
import com.drawing.drawing.entity.Mentor;
import lombok.Getter;


@Getter
public class FeedbackReqeustDto {

    private String title;
    private String description;
    private int price;
    private String feedback_file_type;


    /*
    public Feedback toEntity(Mentor mentor, Drawing drawing, String uuid, String filename) {
        return Feedback.builder()
                .mentor(mentor)
                .drawing(drawing)
                .title(title)
                .description(description)
                .price(price)
                .uuid(uuid)
                .filename(filename)
                .completeDate(LocalDate.now())
                .build();
    }
     */

}
