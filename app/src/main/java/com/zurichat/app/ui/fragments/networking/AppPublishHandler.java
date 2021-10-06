package com.zurichat.app.ui.fragments.networking;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.zurichat.app.models.User;
import com.zurichat.app.ui.fragments.model.Data;
import com.zurichat.app.ui.fragments.viewmodel.ChannelMessagesViewModel;
import com.zurichat.app.ui.fragments.viewmodel.ChannelViewModel;
import com.zurichat.app.ui.fragments.viewmodel.SharedChannelViewModel;

import java.nio.charset.StandardCharsets;

import centrifuge.PublishEvent;
import centrifuge.PublishHandler;
import centrifuge.Subscription;

public class AppPublishHandler implements PublishHandler {
    protected Activity context;
    private ChannelMessagesViewModel channelMessagesViewModel;
    private ChannelViewModel channelViewModel;
    private SharedChannelViewModel sharedChannelViewModel;
    private final User user;

    public AppPublishHandler(Context context, User user, ChannelMessagesViewModel channelMessagesViewModel) {
        this.context = (Activity) context;
        this.channelMessagesViewModel = channelMessagesViewModel;
        this.user = user;
    }

    public AppPublishHandler(Context context, User user, ChannelViewModel channelViewModel) {
        this.context = (Activity) context;
        this.channelViewModel = channelViewModel;
        this.user = user;
    }

    public AppPublishHandler(Context context, User user, SharedChannelViewModel sharedChannelViewModel) {
        this.context = (Activity) context;
        this.sharedChannelViewModel = sharedChannelViewModel;
        this.user = user;
    }

    @Override
    public void onPublish(final Subscription sub, final PublishEvent event) {
        String dataString = new String(event.getData(), StandardCharsets.UTF_8);
        Data data = new Gson().fromJson(dataString,Data.class);

        if (channelMessagesViewModel!=null){
            channelMessagesViewModel.receiveMessage(data);
        }

        if (channelViewModel!=null){
            channelViewModel.notifyList(data);
        }

        if (sharedChannelViewModel!=null){
            sharedChannelViewModel.receiveNewMessage(data);
        }
    }

}
