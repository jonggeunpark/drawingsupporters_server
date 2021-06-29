package com.drawing.drawing.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Post {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<Comment> commentSet;

    private String title;
    private String content;
    private int viewCount;
    private int likeCount;

    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;

}
