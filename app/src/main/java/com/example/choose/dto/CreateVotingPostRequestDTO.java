package com.example.choose.dto;

import java.util.List;

public class CreateVotingPostRequestDTO {
    private List<String> options;

    private String title;

    public CreateVotingPostRequestDTO() { }

    public CreateVotingPostRequestDTO(List<String> options, String title) {
        this.options = options;
        this.title = title;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
