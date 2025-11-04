package com.sehoon.bulletinBoardMVC.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "create_at")
    private LocalDateTime createAt = LocalDateTime.now();


    protected PostEntity() {}

    private PostEntity(String title, String content, Long userId) {
        this.title = title;
        this.content = content;
        this.userId = userId;
    }

    // Factory Method
    public static PostEntity createPostEntity(String title, String content, Long  userId) {
        return new PostEntity(title, content, userId);
    }

    public Long getPostId() {
        return postId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // update Method
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
