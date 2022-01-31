package com.example.choose.dto;

public class ImagePostDTO extends PostDTO {
    private String url;
    private String description;

    public ImagePostDTO(Long id,
                        Long authorId,
                        String authorUsername,
                        String title,
                        PostType type,
                        Integer likesCount,
                        LikeStatus likeStatus,
                        String url,
                        String description) {
        super(id, authorId, authorUsername, title, type, likesCount, likeStatus);
        this.url = url;
        this.description = description;
    }

    public ImagePostDTO() { }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
