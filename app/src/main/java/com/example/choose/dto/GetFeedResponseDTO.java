package com.example.choose.dto;

import java.util.List;

public class GetFeedResponseDTO {
    private List<PostDTO> posts;

    public List<PostDTO> getPosts() {
        return posts;
    }

    public void setPosts(List<PostDTO> posts) {
        this.posts = posts;
    }
}
