package com.example.choose.dto;

public class PetitionPostDTO extends PostDTO {
    private String mediaUrl;
    private String description;
    private Integer goal;
    private Integer votedCount;
    private Boolean voted;

    public PetitionPostDTO(Long id,
                           Long authorId,
                           String authorUsername,
                           String title,
                           PostType type,
                           Integer likesCount,
                           LikeStatus likeStatus,
                           String mediaUrl,
                           String description,
                           Integer goal,
                           Integer votedCount,
                           Boolean voted) {
        super(id, authorId, authorUsername, title, type, likesCount, likeStatus);
        this.mediaUrl = mediaUrl;
        this.description = description;
        this.goal = goal;
        this.votedCount = votedCount;
        this.voted = voted;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getGoal() {
        return goal;
    }

    public void setGoal(Integer goal) {
        this.goal = goal;
    }

    public Integer getVotedCount() {
        return votedCount;
    }

    public void setVotedCount(Integer votedCount) {
        this.votedCount = votedCount;
    }

    public Boolean getVoted() {
        return voted;
    }

    public void setVoted(Boolean voted) {
        this.voted = voted;
    }
}
