package com.drawing.drawing.dto.Drawing;

import com.drawing.drawing.entity.Drawing;
import com.drawing.drawing.entity.DrawingFile;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public class DetailDrawingDto {
    private String title;
    private String description;
    private String nickname;
    private int price_lower_limit;
    private int price_upper_limit;
    private List<String> feedback_type;
    private String feedback_file_type;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate registration_date;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate deadline;

    private List<String> thumbnail_list;
    //private List<URL> download_url;

    public static DetailDrawingDto of(Drawing drawing, String storage) {

        List<String> thumbnailList = new ArrayList<>();
        for(DrawingFile drawingFile: drawing.getDrawingFileSet()) {
            String thumbnail = storage + "/" + drawingFile.getUuid() + drawingFile.getFilename();
            thumbnailList.add(thumbnail);
        }

        return new DetailDrawingDto(drawing.getTitle(), drawing.getDescription(), drawing.getMentee().getNickname(), drawing.getPriceLowerLimit(),
                drawing.getPriceUpperLimit(), Arrays.asList(drawing.getFeedbackType().split(",")),
                drawing.getFeedbackType(), drawing.getRegistrationDate(), drawing.getDeadline(), thumbnailList);
    }

    /*
    public static DetailDrawingDto of(Drawing drawing, List<URL> download_url) {
        return new DetailDrawingDto(drawing.getTitle(), drawing.getDescription(), drawing.getPriceLowerLimit(),
                drawing.getPriceUpperLimit(), Arrays.asList(drawing.getFeedbackType().split(",")),
                drawing.getFeedbackType(), drawing.getRegistrationDate(), download_url);
    }
     */
}
