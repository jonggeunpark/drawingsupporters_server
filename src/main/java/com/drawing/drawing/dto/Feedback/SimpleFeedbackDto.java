package com.drawing.drawing.dto.Feedback;

import com.drawing.drawing.entity.DrawingFile;
import com.drawing.drawing.entity.Feedback;
import com.drawing.drawing.entity.FeedbackFile;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class SimpleFeedbackDto {

    private Long id;
    private String nickname;
    private String title;
    private List<String> thumbnail;

    public static SimpleFeedbackDto of(Feedback feedback, String storage) {

        List<String> thumbnailList = new ArrayList<>();
        for(FeedbackFile feedbackFile: feedback.getFeedbackFileSet()) {
            String thumbnail = storage + "/" + feedbackFile.getUuid() + feedbackFile.getFilename();
            thumbnailList.add(thumbnail);
        }
        return new SimpleFeedbackDto(feedback.getId(), feedback.getMentor().getNickname(), feedback.getTitle(), thumbnailList);
    }
}
