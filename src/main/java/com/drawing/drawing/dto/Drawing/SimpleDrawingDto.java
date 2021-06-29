package com.drawing.drawing.dto.Drawing;

import com.drawing.drawing.dto.Feedback.SimpleFeedbackDto;
import com.drawing.drawing.entity.Drawing;
import com.drawing.drawing.entity.Feedback;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SimpleDrawingDto {

    private Long id;
    private String title;
    private String thumbnail;

    public static SimpleDrawingDto of(Drawing drawing) {
        return new SimpleDrawingDto(drawing.getId(), drawing.getTitle(), drawing.getImageSrc());
    }
}
