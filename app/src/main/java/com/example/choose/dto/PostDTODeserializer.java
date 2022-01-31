package com.example.choose.dto;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class PostDTODeserializer implements JsonDeserializer<PostDTO> {
    private final Gson gson = new Gson();

    @Override
    public PostDTO deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject postDTO = jsonElement.getAsJsonObject();
        PostType postType = PostType.valueOf(postDTO.get("type").getAsString());
        switch (postType) {
            case TEXT:
                return gson.fromJson(jsonElement, TextPostDTO.class);
            case IMAGE:
                return gson.fromJson(jsonElement, ImagePostDTO.class);
            case PETITION:
                return gson.fromJson(jsonElement, PetitionPostDTO.class);
            case VOTE:
                return gson.fromJson(jsonElement, VotingPostDTO.class);
            case PLAYOFF:
                return gson.fromJson(jsonElement, PlayOffPostDTO.class);
            default:
                throw new IllegalArgumentException("invalid post type");
        }
    }
}
