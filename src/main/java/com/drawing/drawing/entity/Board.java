package com.drawing.drawing.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Board {

    @Id @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private Set<Post> postSet = new HashSet<>();

    private String name;
}
