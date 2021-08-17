package com.drawing.drawing.dto.Drawing;

import com.drawing.drawing.dto.Feedback.DetailFeedbackDto;
import com.drawing.drawing.dto.Feedback.FeedbackAndDrawingInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class DrawingAndFeedbackListInfo {

    private DetailDrawingDto drawing_info;
    private List<DetailFeedbackDto> feedback_info_list;

    public static DrawingAndFeedbackListInfo of(DetailDrawingDto detailDrawingDto, List<DetailFeedbackDto> detailFeedbackDto) {
        return new DrawingAndFeedbackListInfo(detailDrawingDto, detailFeedbackDto);
    }
}
