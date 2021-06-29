package com.drawing.drawing.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@DiscriminatorValue("e")
public class Mentee extends User{

    @OneToMany(mappedBy = "mentee", cascade = CascadeType.ALL)
    private Set<Drawing> drawingSet = new HashSet<>();

    private String job;
    private String desiredField;
    private String pathToSite;
    private boolean marketing_yn;

    /*
    //== 빌더 ==//
    @Builder
    public Mentee(Set<Drawing> drawingSet, String job, String desiredField, String pathToSite, boolean marketing_yn) {
        this.drawingSet = drawingSet;
        this.job = job;
        this.desiredField = desiredField;
        this.pathToSite = pathToSite;
        this.marketing_yn = marketing_yn;
    }

     */
}
