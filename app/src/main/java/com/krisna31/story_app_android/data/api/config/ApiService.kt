package com.krisna31.story_app_android.data.api.config

import com.krisna31.story_app_android.data.api.response.DetailStoryResponse
import com.krisna31.story_app_android.data.api.response.LoginResponse
import com.krisna31.story_app_android.data.api.response.RegisterResponse
import com.krisna31.story_app_android.data.api.response.StoryResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
//    @Multipart
//    @POST("/v1/stories/guest")
//    fun uploadImage(
//        @Part file: MultipartBody.Part,
//        @Part("description") description: RequestBody,
//    ): Call<FileUploadResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") username: String,
        @Field("password") password: String,
    ): Call<LoginResponse>

    @GET("stories")
    fun getStories(
        @Header("Authorization") token: String,
    ): Call<StoryResponse>

    @GET("stories/{id}")
    fun getStory(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): Call<DetailStoryResponse>
}