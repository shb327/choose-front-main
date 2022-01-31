package com.example.choose.dto;

import java.io.Serializable;

public class VotingOptionDTO implements Serializable {
    private Long id;
    private String title;
    private Integer votedUsers;
    private Boolean voted;

    public VotingOptionDTO(){
    }

    public VotingOptionDTO(Long id, String title, Integer votedUsers, Boolean voted) {
        this.id = id;
        this.title = title;
        this.votedUsers = votedUsers;
        this.voted = voted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getVotedUsers() {
        return votedUsers;
    }

    public void setVotedUsers(Integer votedUsers) {
        this.votedUsers = votedUsers;
    }

    public Boolean getVoted() {
        return voted;
    }

    public void setVoted(Boolean voted) {
        this.voted = voted;
    }

    @Override
    public String toString() {
        return "VotingOptionDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", votedUsers=" + votedUsers +
                ", voted=" + voted +
                '}';
    }
}
