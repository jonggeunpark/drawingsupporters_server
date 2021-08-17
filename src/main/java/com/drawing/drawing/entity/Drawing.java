package com.drawing.drawing.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(mappedBy = "drawing", cascade = CascadeType.ALL)
    private Set<Feedback> feedbackSet = new HashSet<>();

    @OneToMany(mappedBy = "drawing", cascade = CascadeType.ALL)
    private Set<DrawingFile> drawingFileSet = new HashSet<>();

    private String title;
    private String description;
    private String feedbackType;
    private int priceUpperLimit;
    private int priceLowerLimit;
    private String phoneNumber;
    private DrawingStatus drawingStatus;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate registrationDate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate deadline;

    //== 빌더 ==//
    @Builder
    public Drawing(Mentee mentee, Set<Feedback> feedbackSet, String title, String description, String feedbackType, int priceUpperLimit,
                   int priceLowerLimit, String phoneNumber, LocalDate registrationDate, LocalDate deadline,
                   Set<DrawingFile> drawingFileSet, DrawingStatus drawingStatus) {

        this.mentee = mentee;
        mentee.getDrawingSet().add(this);

        this.feedbackSet = null;
        this.title = title;
        this.drawingStatus = drawingStatus;
        this.description = description;
        this.feedbackType = feedbackType;
        this.priceLowerLimit = priceLowerLimit;
        this.priceUpperLimit = priceUpperLimit;
        this.phoneNumber = phoneNumber;
        this.registrationDate = registrationDate;
        this.deadline = deadline;
        this.drawingFileSet = drawingFileSet;
    }

    public void changeDrawingStatus(DrawingStatus drawingStatus){
        this.drawingStatus = drawingStatus;
    }
}
