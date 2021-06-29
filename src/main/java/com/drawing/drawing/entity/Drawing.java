package com.drawing.drawing.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @OneToOne
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    private String title;
    private String imageSrc;
    private String description;
    private String feedbackType;
    private String file;
    private int priceUpperLimit;
    private int priceLowerLimit;
    private String phoneNumber;

    @JsonFormat(pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDateTime registDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime endDate;

    //== 빌더 ==//
    @Builder
    public Drawing(Mentee mentee, String title, String imageSrc, String description,
                   String feedbackType, int priceUpperLimit, int priceLowerLimit, String phoneNumber,
                   LocalDateTime registDate, String file, LocalDateTime endDate) {

        this.mentee = mentee;
        mentee.getDrawingSet().add(this);

        this.title = title;
        this.file = file;
        this.imageSrc = imageSrc;
        this.description = description;
        this.feedbackType = feedbackType;
        this.priceLowerLimit = priceLowerLimit;
        this.priceUpperLimit = priceUpperLimit;
        this.phoneNumber = phoneNumber;
        this.registDate = registDate;
        this.endDate = endDate;
    }

}
