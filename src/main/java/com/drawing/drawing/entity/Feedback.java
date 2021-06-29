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

    @OneToOne(mappedBy = "feedback")
    private Drawing drawing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Mento mento;

    private String title;
    private FeedbackStatus status;
    private String description;
    private int price;
    private String thumbnail;
    private String feedbackType;
    private String file;

    @JsonFormat(pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDateTime registDate;

    //== 빌더 ==//
    @Builder
    public Feedback (Drawing drawing, Mento mento, String title, FeedbackStatus status, String description, int price, String thumbnail, String feedbackType, String file, LocalDateTime registDate) {
        this.drawing = drawing;

        this.mento = mento;
        mento.getFeedbackSet().add(this);

        this.title = title;
        this.status = status;
        this.description = description;
        this.price = price;
        this.thumbnail = thumbnail;
        this.feedbackType = feedbackType;
        this.file = file;
        this.registDate = registDate;
    }
}
