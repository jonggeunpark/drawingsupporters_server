package com.drawing.drawing.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("o")
public class Mentor extends User{

    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL)
    private Set<Feedback> feedbackSet = new HashSet<>();

    /*
    //== 빌더 ==//
    @Builder
    public Mento (Set<Feedback> feedbackSet) {
        this.feedbackSet = feedbackSet;
    }
    */
}
