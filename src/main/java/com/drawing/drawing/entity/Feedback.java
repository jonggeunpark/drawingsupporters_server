package com.drawing.drawing.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Feedback {

    @Id @GeneratedValue
    @Column(name = "feedback_id")
    private Long id;

    @OneToOne(mappedBy = "drawing")
    private Drawing drawing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Mento mento;

    private FeedbackStatus status;
    private String description;
    private int price;
    private LocalDateTime registDate;
}
