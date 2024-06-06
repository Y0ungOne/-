package com.example.missingapp;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface UserService {
    @POST("/sign-in")
    Call<LoginResponse> signIn(@Body LoginRequest loginRequest);

    @POST("/sign-up")
    Call<UserSignUpResponse> signUp(@Body User signUpRequest);

    /*@GET("/member")
    Call<UserProfileResponse> getUserProfile();

    @PATCH("/member/password")
    Call<Void> updatePassword(@Body PasswordUpdate passwordUpdate);

    @PATCH("/member/nick-name")
    Call<Void> updateNickName(@Body NickNameUpdate nickNameUpdate);

    @DELETE("/member")
    Call<Void> deleteUser();*/

    @GET("/detect")
    Call<ResponseBody> getProtectedTargetImage(@Query("targetId") int targetId);

    @Multipart
    @POST("/protected-target")
    Call<Void> registerProtectedTarget(
            @Part("createDto") RequestBody createDto,
            @Part MultipartBody.Part file
    );

    @GET("/protected-targets")
    Call<ProtectedTargetResponse> getPhotos();

    @GET("/protected-target")
    Call<Photo> getPhoto(@Query("protectedTargetId") int id);

    @PATCH("/protected-target")
    Call<Photo> updatePhoto(@Body Photo photo);

    @HTTP(method = "DELETE", path = "/protected-target", hasBody = true)
    Call<Void> deletePhoto(@Body ProtectedTargetDeleteDto deleteDto);

    @GET("detect-info")
    Call<String[]> getDetectInfo();
}



