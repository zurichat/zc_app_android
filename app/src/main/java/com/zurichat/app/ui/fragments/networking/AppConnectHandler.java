package com.zurichat.app.ui.fragments.networking;

import android.app.Activity;
import android.content.Context;

import com.zurichat.app.models.User;
import com.zurichat.app.ui.fragments.model.RoomData;
import com.zurichat.app.ui.fragments.viewmodel.ChannelMessagesViewModel;
import com.zurichat.app.ui.fragments.viewmodel.ChannelViewModel;
import com.zurichat.app.ui.fragments.viewmodel.SharedChannelViewModel;

import centrifuge.Client;
import centrifuge.ConnectEvent;
import centrifuge.ConnectHandler;
import centrifuge.Subscription;

public class AppConnectHandler implements ConnectHandler {
    protected Activity context;
    private RoomData roomData;
    private ChannelMessagesViewModel channelMessagesViewModel;
    private ChannelViewModel channelViewModel;
    private SharedChannelViewModel sharedChannelViewModel;
    private final User user;
    private Subscription sub;

    public AppConnectHandler(Context context, User user, RoomData room, ChannelMessagesViewModel channelMessagesViewModel) {
        this.context = (Activity) context;
        this.roomData = room;
        this.channelMessagesViewModel = channelMessagesViewModel;
        this.user = user;
    }

    public AppConnectHandler(Context context, User user, ChannelViewModel channelViewModel) {
        this.context = (Activity) context;
        this.channelViewModel = channelViewModel;
        this.user = user;
    }

    public AppConnectHandler(Context context, User user, SharedChannelViewModel sharedChannelViewModel) {
        this.context = (Activity) context;
        this.sharedChannelViewModel = sharedChannelViewModel;
        this.user = user;
    }

    @Override
    public void onConnect(Client client, final ConnectEvent event) {
        context.runOnUiThread(() -> {
            if (channelMessagesViewModel!=null){
                /*channelMessagesViewModel.isConnected(true);
                AppPublishHandler publishHandler = new AppPublishHandler(context,user,channelMessagesViewModel);
                try {
                    sub = client.newSubscription(roomData.getSocket_name());

                    sub.onPublish(publishHandler);
                    sub.subscribe();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }

            if (channelViewModel!=null && sendConnectionUpdates){
                sendConnectionUpdates = false;
                channelViewModel.isConnected(true);
            }
        });
    }

    boolean sendConnectionUpdates = true;
    public void disconnect(Client client){
        if (sub!=null){
            try {
                sub.unsubscribe();
                client.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
