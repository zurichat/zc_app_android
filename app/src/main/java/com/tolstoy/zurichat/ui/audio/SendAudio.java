package com.tolstoy.zurichat.ui.audio;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface SendAudio {

    @Multipart
    @POST("api/v1/org/{org_id}/rooms/{room_id}/messagemedia")
    Call<RequestBody> sendAudio(@Part MultipartBody.Part pat, @Part("messagemedia") RequestBody messagemedia);

}
