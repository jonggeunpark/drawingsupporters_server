package com.drawing.drawing.dto.Feedback;

import com.drawing.drawing.dto.Drawing.DetailDrawingDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FeedbackAndDrawingInfoDto {

    private DetailFeedbackDto feedbackInfo;
    private DetailDrawingDto drawingInfo;

    public static FeedbackAndDrawingInfoDto of(DetailFeedbackDto detailFeedbackDto, DetailDrawingDto detailDrawingDto) {
        return new FeedbackAndDrawingInfoDto(detailFeedbackDto, detailDrawingDto);
    }
}
