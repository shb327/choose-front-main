package com.example.choose.dto;

import java.io.Serializable;

public class PlayOffOptionDTO implements Serializable {
    private Long id;
    private String title;
    private String media;
    private Integer votedUsers;
    private Boolean voted;

    public PlayOffOptionDTO(){ }

    public PlayOffOptionDTO(Long id, String title, Integer votedUsers, Boolean voted, String media) {
        this.id = id;
        this.title = title;
        this.votedUsers = votedUsers;
        this.media = media;
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

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    @Override
    public String toString() {
        return "PlayOffOptionDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", media='" + media + '\'' +
                ", votedUsers=" + votedUsers +
                ", voted=" + voted +
                '}';
    }
}