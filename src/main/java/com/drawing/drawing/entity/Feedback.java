package com.drawing.drawing.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

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
    private FeedbackStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate acceptDate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate completeDate;


    /*
    //== 빌더 ==//
    @Builder
    public Feedback (Drawing drawing, Mento mento, String title, String description, int price, String feedbackFileType,
                     String uuid, String filename, LocalDate acceptDate, LocalDate completeDate) {
        this.drawing = drawing;

        this.mento = mento;
        mento.getFeedbackSet().add(this);

        this.title = title;
        this.description = description;
        this.price = price;
        this.feedbackFileType = feedbackFileType;
        this.uuid = uuid;
        this.filename = filename;
        this.acceptDate = acceptDate;
        this.completeDate = completeDate;
    }
     */

    //== 빌더 ==//
    @Builder
    public Feedback (Drawing drawing, Mento mento, LocalDate acceptDate) {
        this.drawing = drawing;
        drawing.setFeedback(this);
        this.mento = mento;
        mento.getFeedbackSet().add(this);
        this.acceptDate = acceptDate;
        this.status = FeedbackStatus.ACCEPTED;
    }

    public void completeFeedback(String title, String description, int price,
                                 String feedbackFileType, String uuid, String filename, LocalDate completeDate) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.feedbackFileType = feedbackFileType;
        this.uuid = uuid;
        this.filename = filename;
        this.completeDate = completeDate;
        this.status = FeedbackStatus.COMPLETED;
        this.drawing.changeDrawingStatus(DrawingStatus.COMPLETED);
    }

}
