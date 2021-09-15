package com.tolstoy.zurichat.ui.fragments.networking;

import com.tolstoy.zurichat.models.ChannelModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/****
 * This Class contains all Channels APIs.
 * All subsequent APIs concerning channels will be added here
 */
public interface ChannelsList {
    @GET("v1/1/channels/")
    Call<List<ChannelModel>> getChannelList();
}
