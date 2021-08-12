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
    private String nickname;
    private String title;
    private String thumbnail;
    private String feedback_type;


    public static SimpleDrawingDto of(Drawing drawing, String storage) {

        return new SimpleDrawingDto(drawing.getId(), drawing.getMentee().getNickname(), drawing.getTitle(), storage + "/" + drawing.getUuid() + drawing.getFilename(), drawing.getFeedbackType());
    }
}
