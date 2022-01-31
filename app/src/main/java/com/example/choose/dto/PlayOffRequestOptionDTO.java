package com.example.choose.dto;

import java.io.Serializable;

public class PlayOffRequestOptionDTO implements Serializable {
    private String title;

    public PlayOffRequestOptionDTO(){ }

    public PlayOffRequestOptionDTO(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "PlayOffRequestOptionDTO{" +
                "title='" + title + '\'' +
                '}';
    }
}