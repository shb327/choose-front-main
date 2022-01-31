package com.example.choose.dto;

public class GetUserFeedRequestDTO {
    private Integer page;
    private Integer size;
    private Integer userId;

    public GetUserFeedRequestDTO(Integer page, Integer size, Integer userId) {
        this.page = page;
        this.size = size;
        this.userId = userId;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
