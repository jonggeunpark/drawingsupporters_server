package com.drawing.drawing.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedbackFile {

    @Id
    @GeneratedValue
    @Column(name = "feedbackFile_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    private String filename;  // 파일명
    private String uuid; // uuid

    //== 빌더 ==//
    @Builder
    public FeedbackFile(Feedback feedback, String filename, String uuid) {

        this.feedback = feedback;
        feedback.getFeedbackFileSet().add(this);

        this.filename = filename;
        this.uuid = uuid;
    }

}

