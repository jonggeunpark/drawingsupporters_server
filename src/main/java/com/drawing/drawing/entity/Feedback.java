package com.drawing.drawing.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    private FeedbackStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate acceptDate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate completeDate;

    @OneToMany(mappedBy = "feedback", cascade = CascadeType.ALL)
    private Set<FeedbackFile> feedbackFileSet = new HashSet<>();

    //== 빌더 ==//
    @Builder
    public Feedback (Drawing drawing, Mento mento, LocalDate acceptDate, Set<FeedbackFile> feedbackFileSet) {
        this.drawing = drawing;
        drawing.setFeedback(this);
        this.mento = mento;
        mento.getFeedbackSet().add(this);
        this.acceptDate = acceptDate;
        this.status = FeedbackStatus.ACCEPTED;
        this.feedbackFileSet = feedbackFileSet;
    }

    public void completeFeedback(String title, String description, int price,
                                 String feedbackFileType, LocalDate completeDate) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.feedbackFileType = feedbackFileType;
        this.completeDate = completeDate;
        this.status = FeedbackStatus.COMPLETED;
        this.drawing.changeDrawingStatus(DrawingStatus.COMPLETED);
    }

}
