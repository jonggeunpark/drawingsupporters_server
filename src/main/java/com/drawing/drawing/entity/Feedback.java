package com.drawing.drawing.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feedback {

    @Id @GeneratedValue
    @Column(name = "feedback_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "drawing_id")
    private Drawing drawing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Mento mento;

    private String title;
    private String description;
    private int price;
    private String feedbackFileType;
    private String uuid;
    private String filename;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime registDate;

    //== 빌더 ==//
    @Builder
    public Feedback (Drawing drawing, Mento mento, String title, String description, int price, String feedbackFileType, String uuid, String filename, LocalDateTime registDate) {
        this.drawing = drawing;

        this.mento = mento;
        mento.getFeedbackSet().add(this);

        this.title = title;
        this.description = description;
        this.price = price;
        this.feedbackFileType = feedbackFileType;
        this.uuid = uuid;
        this.filename = filename;
        this.registDate = registDate;
    }
}
