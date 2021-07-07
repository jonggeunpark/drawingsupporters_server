package com.drawing.drawing.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Drawing {

    @Id @GeneratedValue
    @Column(name = "drawing_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Mentee mentee;

    @OneToOne(mappedBy = "drawing")
    private Feedback feedback;

    private String title;
    private String description;
    private String feedbackType;
    private String uuid;
    private String filename;
    private int priceUpperLimit;
    private int priceLowerLimit;
    private String phoneNumber;
    private DrawingStatus drawingStatus;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate registDate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate endDate;

    //== 빌더 ==//
    @Builder
    public Drawing(Mentee mentee, Feedback feedback, String title, String description, String feedbackType, int priceUpperLimit,
                   int priceLowerLimit, String phoneNumber, LocalDate registDate, String uuid, String filename,
                   LocalDate endDate, DrawingStatus drawingStatus) {

        this.mentee = mentee;
        mentee.getDrawingSet().add(this);

        this.feedback = null;
        this.title = title;
        this.drawingStatus = drawingStatus;
        this.description = description;
        this.feedbackType = feedbackType;
        this.priceLowerLimit = priceLowerLimit;
        this.priceUpperLimit = priceUpperLimit;
        this.uuid = uuid;
        this.filename = filename;
        this.phoneNumber = phoneNumber;
        this.registDate = registDate;
        this.endDate = endDate;
    }

    public void ChangeDrawingStatus(DrawingStatus drawingStatus){
        this.drawingStatus = drawingStatus;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }
}
