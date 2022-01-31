package com.example.choose.api;

import com.example.choose.dto.CreateTextPostRequestDTO;
import com.example.choose.dto.CreateVotingPostRequestDTO;
import com.example.choose.dto.GetCommunityFeedRequestDTO;
import com.example.choose.dto.GetCommunityFeedResponseDTO;
import com.example.choose.dto.GetFeedRequestDTO;
import com.example.choose.dto.GetFeedResponseDTO;
import com.example.choose.dto.GetUserFeedRequestDTO;
import com.example.choose.dto.LikeStatus;
import com.example.choose.dto.PlayOffOptionDTO;
import com.example.choose.dto.PlayOffRequestOptionDTO;
import com.example.choose.dto.PostDTO;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PostController {
    @GET("/api/posts/{id}")
    Call<PostDTO> getPost (@Path("id") int id);

    @POST("/api/posts/create/text")
    Call<PostDTO> createTextPost(@Body CreateTextPostRequestDTO createTextPostRequestDTO);

    @Multipart
    @POST("/api/posts/create/image")
    Call<PostDTO> createImagePost(@Query("title")String title,
                                  @Query("description")String  description,
                                  @Part MultipartBody.Part file);

    @Multipart
    @POST("/api/posts/create/petition")
    Call<PostDTO> createPetitionPost(@Query("title")String title,
                                     @Query("description")String  description,
                                     @Part MultipartBody.Part file,
                                     @Query("goal")Integer goal);

    @POST("/api/posts/create/petition")
    Call<PostDTO> createPetitionPost(@Query("title")String title,
                                     @Query("description")String  description,
                                     @Query("goal")Integer goal);

    @Multipart
    @POST("/api/posts/create/playoff")
    Call<PostDTO> createPlayoffPost(@Part("title")RequestBody title,
                                    @Part("options")RequestBody options,
                                    @Part List<MultipartBody.Part> files);

    @POST("/api/posts/create/voting")
    Call<PostDTO> createVotingPost(@Body CreateVotingPostRequestDTO createVotingPostRequestDTO);

    @POST("/api/posts/feed")
    Call<GetFeedResponseDTO> getFeed (@Body GetFeedRequestDTO getFeedRequestDTO);

    @POST("/api/posts/feed/community")
    Call<GetCommunityFeedResponseDTO> getCommunityFeed (@Body GetCommunityFeedRequestDTO getCommunityFeedRequestDTO);

    @POST("/api/posts/feed/user")
    Call<GetFeedResponseDTO> getUserFeed (@Body GetUserFeedRequestDTO getUserFeedRequestDTO);

    @POST("/api/posts/feed/self")
    Call<GetFeedResponseDTO> getSelfFeed (@Body GetFeedRequestDTO getFeedRequestDTO);

    @POST("/api/posts/like/{id}")
    Call<Void> like (@Path("id") int id, @Query("status") LikeStatus status);

    @DELETE("/api/posts/{id}")
    Call<Void> deletePost (@Path("id") int id);

    @PUT("/api/posts/vote/{id}")
    Call<Void> voteForPost (@Path("id") int id, @Query("optionId")Integer optionId);
}