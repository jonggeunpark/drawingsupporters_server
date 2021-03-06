package com.drawing.drawing.dto.Feedback;

import com.drawing.drawing.entity.Drawing;
import com.drawing.drawing.entity.Feedback;
import com.drawing.drawing.entity.FeedbackFile;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public class DetailFeedbackDto {

    private String title;
    private String description;
    private String nickname;
    private int price;
    private List<String> feedback_type;
    private String feedback_file_type;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate registration_date;

    private List<String> thumbnail;

    public static DetailFeedbackDto of(Feedback feedback, String storage) {
        Drawing drawing = feedback.getDrawing();

        List<String> thumbnailList = new ArrayList<>();
        for(FeedbackFile feedbackFile: feedback.getFeedbackFileSet()) {
            String thumbnail = storage + "/" + feedbackFile.getUuid() + feedbackFile.getFilename();
            thumbnailList.add(thumbnail);
        }

        return new DetailFeedbackDto(feedback.getTitle(), feedback.getDescription(), feedback.getMentor().getNickname(),
                feedback.getPrice(), Arrays.asList(drawing.getFeedbackType().split(",")),
                feedback.getDrawing().getFeedbackType(), feedback.getRegistrationDate(), thumbnailList);
    }
}
