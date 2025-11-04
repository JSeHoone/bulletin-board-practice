package com.sehoon.bulletinBoardMVC.model.dto;

public class PostCreateRequestDTO {
    private String title;
    private String content;

    public PostCreateRequestDTO() {}

    public PostCreateRequestDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "PostCreateRequestDTO{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
