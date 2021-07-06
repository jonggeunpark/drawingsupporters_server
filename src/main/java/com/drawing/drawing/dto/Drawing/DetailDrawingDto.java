package com.drawing.drawing.dto.Drawing;

import com.drawing.drawing.entity.Drawing;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public class DetailDrawingDto {
    private String title;
    private String description;
    private int price_lower_limit;
    private int price_upper_limit;
    private List<String> feedback_type;
    private String feedback_file_type;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime end_time;

    public static DetailDrawingDto of(Drawing drawing) {
        return new DetailDrawingDto(drawing.getTitle(), drawing.getDescription(), drawing.getPriceLowerLimit(),
                drawing.getPriceUpperLimit(), Arrays.asList(drawing.getFeedbackType().split(",")),
                drawing.getFeedbackType(), drawing.getRegistDate());
    }
}
