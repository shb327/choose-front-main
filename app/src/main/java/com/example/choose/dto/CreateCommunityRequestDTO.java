package com.example.choose.dto;

public class CreateCommunityRequestDTO {
    private String description;
    private String name;
    private String username;

    public CreateCommunityRequestDTO(String description, String name, String username) {
        this.description = description;
        this.name = name;
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
