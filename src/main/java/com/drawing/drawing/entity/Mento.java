package com.drawing.drawing.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("o")
public class Mento extends User{

    @OneToMany(mappedBy = "feedback", cascade = CascadeType.ALL)
    private Set<Feedback> feedbackSet = new HashSet<>();

}
