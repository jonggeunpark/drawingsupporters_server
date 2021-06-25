package com.drawing.drawing.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("e")
public class Mentee extends User{

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Drawing> drawingSet = new HashSet<>();

    private String job;
    private String feedback_field_webtoon;
    private String feedback_field_drawing;
    private boolean marketing_yn;
}
