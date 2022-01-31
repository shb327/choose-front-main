package com.example.choose.api;

import com.example.choose.dto.CommunityDTO;
import com.example.choose.dto.CreateCommunityRequestDTO;
import com.example.choose.dto.EditCommunityRequestDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CommunityController {
    @GET("/api/communities")
    Call<List<CommunityDTO>> getAllCommunities(@Query("page") int page, @Query("size") int size);

    @GET("/api/communities/user/all")
    Call<List<CommunityDTO>> getUserCommunities();

    @GET("/api/communities/user/owning")
    Call<List<CommunityDTO>> getUserOwning();

    @GET("/api/communities/discover")
    Call<List<CommunityDTO>> getDiscoverCommunity(@Query("page") int page, @Query("size") int size);

    @POST("/api/communities/post/add")
    Call<Void> addPost (@Query("communityId") int communityId, @Query("postId") int postId);

    @POST("/api/communities/join")
    Call<Void> joinCommunity (@Query("communityId") int communityId);

    @POST("/api/communities")
    Call<Void> createCommunity (@Body CreateCommunityRequestDTO editCommunityRequestDTO);

    @PUT("/api/communities/{id}")
    Call<Void> updateCommunity (@Path("id") int id, @Body EditCommunityRequestDTO editCommunityRequestDTO);

    @DELETE("/api/communities/{id}")
    Call<Void> deleteCommunity (@Path("id") int id);

    @POST("/api/communities/leave")
    Call<Void> leaveCommunity (@Query("communityId") int communityId);
}
