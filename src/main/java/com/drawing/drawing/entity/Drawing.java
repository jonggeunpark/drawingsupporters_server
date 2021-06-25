package com.drawing.drawing.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Drawing {

    @Id @GeneratedValue
    @Column(name = "drawing_id")

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Mentee mentee;

    @OneToOne
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    private String imageSrc;
    private String description;
    private int price;
    private LocalDateTime registDate;
}
