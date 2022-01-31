package com.example.choose.dto;

public class GetCommunityFeedRequestDTO {
    private Integer page;
    private Integer size;
    private Integer communityId;

    public GetCommunityFeedRequestDTO(Integer page, Integer size, Integer communityId) {
        this.page = page;
        this.size = size;
        this.communityId = communityId;
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

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }
}
