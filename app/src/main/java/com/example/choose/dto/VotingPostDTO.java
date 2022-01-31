package com.example.choose.dto;

import java.util.List;

public class VotingPostDTO extends PostDTO{
    private List<VotingOptionDTO> options;

    public VotingPostDTO(Long id,
                         Long authorId,
                         String authorUsername,
                         String title,
                         PostType type,
                         Integer likesCount,
                         LikeStatus likeStatus,
                         List<VotingOptionDTO> options) {
        super(id, authorId, authorUsername, title, type, likesCount, likeStatus);
        this.options = options;
    }

    public List<VotingOptionDTO> getOptions() {
        return options;
    }

    public void setOptions(List<VotingOptionDTO> options) {
        this.options = options;
    }
}
