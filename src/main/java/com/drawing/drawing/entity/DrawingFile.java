package com.drawing.drawing.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DrawingFile {

    @Id @GeneratedValue
    @Column(name = "drawingFile_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "drawing_id")
    private Drawing drawing;

    private String filename;  // 파일명
    private String uuid; // uuid

    //== 빌더 ==//
    @Builder
    public DrawingFile(Drawing drawing, String filename, String uuid) {

        this.drawing = drawing;
        drawing.getDrawingFileSet().add(this);

        this.filename = filename;
        this.uuid = uuid;
    }

}
