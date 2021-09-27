package com.tolstoy.zurichat.ui.fragments.networking;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.tolstoy.zurichat.models.ChannelModel;
import com.tolstoy.zurichat.models.User;
import com.tolstoy.zurichat.ui.fragments.model.RoomData;
import com.tolstoy.zurichat.ui.fragments.viewmodel.ChannelMessagesViewModel;

import centrifuge.Client;
import centrifuge.ConnectEvent;
import centrifuge.ConnectHandler;
import centrifuge.ErrorEvent;
import centrifuge.ErrorHandler;
import centrifuge.MessageEvent;
import centrifuge.MessageHandler;
import centrifuge.PrivateSubEvent;
import centrifuge.PrivateSubHandler;
import centrifuge.ServerJoinEvent;
import centrifuge.ServerJoinHandler;
import centrifuge.ServerPublishEvent;
import centrifuge.ServerPublishHandler;
import centrifuge.SubscribeErrorEvent;
import centrifuge.SubscribeErrorHandler;
import centrifuge.SubscribeSuccessEvent;
import centrifuge.SubscribeSuccessHandler;
import centrifuge.Subscription;

public class AppConnectHandler implements ConnectHandler {
    protected Activity context;
    private final RoomData roomData;
    private final ChannelMessagesViewModel channelMessagesViewModel;
    private final User user;

    public AppConnectHandler(Context context, User user, RoomData room, ChannelMessagesViewModel channelMessagesViewModel) {
        this.context = (Activity) context;
        this.roomData = room;
        this.channelMessagesViewModel = channelMessagesViewModel;
        this.user = user;
    }

    @Override
    public void onConnect(Client client, final ConnectEvent event) {
        context.runOnUiThread(() -> {
            channelMessagesViewModel.isConnected(true);
            AppPublishHandler publishHandler = new AppPublishHandler(context,user,channelMessagesViewModel);
            Subscription sub;
            try {
                sub = client.newSubscription(roomData.getSocket_name());

                sub.onPublish(publishHandler);
                sub.subscribe();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
