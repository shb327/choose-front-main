package com.example.choose.dto;

public class TextPostDTO extends PostDTO{
    private String content;

    public TextPostDTO() { }

    public TextPostDTO(Long id,
                       Long authorId,
                       String authorUsername,
                       String title,
                       PostType type,
                       Integer likesCount,
                       LikeStatus likeStatus,
                       String content) {
        super(id, authorId, authorUsername, title, type, likesCount, likeStatus);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
