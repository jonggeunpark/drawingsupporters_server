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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drawing_id")
    private Drawing drawing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Mentor mentor;

    private String title;
    private String description;
    private int price;
    private String feedbackFileType;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate registrationDate;

    @OneToMany(mappedBy = "feedback", cascade = CascadeType.ALL)
    private Set<FeedbackFile> feedbackFileSet = new HashSet<>();

    //== 빌더 ==//
    @Builder
    public Feedback (Drawing drawing, Mentor mentor, String title, String description, int price, LocalDate registrationDate, Set<FeedbackFile> feedbackFileSet) {
        this.drawing = drawing;
        drawing.getFeedbackSet().add(this);
        this.mentor = mentor;
        mentor.getFeedbackSet().add(this);

        this.title = title;
        this.description = description;
        this.price = price;
        this.registrationDate = registrationDate;
        this.feedbackFileSet = new HashSet<>();
    }
}
