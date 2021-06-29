package com.drawing.drawing.dto.Feedback;

import com.drawing.drawing.entity.Feedback;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SimpleFeedbackDto {

    private Long id;
    private String title;
    private String thumbnail;

    public static SimpleFeedbackDto of(Feedback feedback) {
        return new SimpleFeedbackDto(feedback.getId(), feedback.getTitle(), feedback.getThumbnail());
    }
}
