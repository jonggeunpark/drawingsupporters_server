package com.drawing.drawing.dto.Feedback;

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
                .feedbackFileType(feedback_file_type)
                .uuid(uuid)
                .filename(filename)
                .completeDate(LocalDate.now())
                .build();
    }
*/
}
