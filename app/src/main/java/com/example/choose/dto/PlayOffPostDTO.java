package com.example.choose.dto;

import java.util.List;

public class PlayOffPostDTO extends PostDTO{
    private List<PlayOffOptionDTO> options;
    private String description;

    public PlayOffPostDTO(Long id,
                          Long authorId,
                          String authorUsername,
                          String title,
                          PostType type,
                          Integer likesCount,
                          LikeStatus likeStatus,
                          List<PlayOffOptionDTO> options,
                          String description) {
        super(id, authorId, authorUsername, title, type, likesCount, likeStatus);
        this.options = options;
        this.description = description;
    }

    public List<PlayOffOptionDTO> getOptions() {
        return options;
    }

    public void setOptions(List<PlayOffOptionDTO> options) {
        this.options = options;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
