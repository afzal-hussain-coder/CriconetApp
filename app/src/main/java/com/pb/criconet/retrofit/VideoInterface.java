package com.pb.criconet.retrofit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface VideoInterface {
    @Multipart
    @POST("new_post")
    Call<ResultObject> uploadVideoToServer(
            @Part MultipartBody.Part video,
            @Part ("user_id") RequestBody user_id,
            @Part ("s") RequestBody s,
            @Part ("postPrivacy") RequestBody postPrivacy,
            @Part ("postText") RequestBody postText
    );
}