package com.example.choose.dto;

import java.io.Serializable;

public class PostDTO implements Serializable {
    private Long id;
    private Long authorId;
    private String authorUsername;
    private String title;
    private PostType type;
    private Integer likesCount;
    private LikeStatus likeStatus;

    public PostDTO() { }

    public PostDTO(Long id, Long authorId, String authorUsername, String title, PostType type,
                   Integer likesCount, LikeStatus likeStatus) {
        this.id = id;
        this.authorId = authorId;
        this.authorUsername = authorUsername;
        this.title = title;
        this.type = type;
        this.likesCount = likesCount;
        this.likeStatus = likeStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PostType getType() {
        return type;
    }

    public void setType(PostType type) {
        this.type = type;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public LikeStatus getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(LikeStatus likeStatus) {
        this.likeStatus = likeStatus;
    }

    @Override
    public String toString() {
        return "PostDTO{" +
                "id=" + id +
                ", authorId=" + authorId +
                ", authorUsername='" + authorUsername + '\'' +
                ", title='" + title + '\'' +
                ", type=" + type +
                '}';
    }
}