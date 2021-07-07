package com.drawing.drawing.dto.Feedback;

import com.drawing.drawing.entity.Drawing;
import com.drawing.drawing.entity.Feedback;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public class DetailFeedbackDto {

    private String title;
    private String description;
    private int price_lower_limit;
    private int price_upper_limit;
    private List<String> feedback_type;
    private String feedback_file_type;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate end_time;

    private URL download_url;

    public static DetailFeedbackDto of(Feedback feedback, URL download_link) {
        Drawing drawing = feedback.getDrawing();
        return new DetailFeedbackDto(feedback.getTitle(), feedback.getDescription(), drawing.getPriceLowerLimit(),
                drawing.getPriceUpperLimit(), Arrays.asList(drawing.getFeedbackType().split(",")),
                feedback.getDrawing().getFeedbackType(), feedback.getCompleteDate(), download_link);
    }

}
