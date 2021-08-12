package com.drawing.drawing.dto.Drawing;

import com.drawing.drawing.dto.Feedback.SimpleFeedbackDto;
import com.drawing.drawing.entity.Drawing;
import com.drawing.drawing.entity.DrawingFile;
import com.drawing.drawing.entity.Feedback;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Getter
public class SimpleDrawingDto {

    private Long id;
    private String nickname;
    private String title;
    private List<String> thumbnail_list;
    private String feedback_type;


    public static SimpleDrawingDto of(Drawing drawing, String storage) {

        List<String> thumbnailList = new ArrayList<>();
        for(DrawingFile drawingFile: drawing.getDrawingFileSet()) {
            String thumbnail = storage + "/" + drawingFile.getUuid() + drawingFile.getFilename();
            thumbnailList.add(thumbnail);
        }
        Set<DrawingFile> drawingFileSet = drawing.getDrawingFileSet();
        return new SimpleDrawingDto(drawing.getId(), drawing.getMentee().getNickname(), drawing.getTitle(), thumbnailList, drawing.getFeedbackType());
    }
}
