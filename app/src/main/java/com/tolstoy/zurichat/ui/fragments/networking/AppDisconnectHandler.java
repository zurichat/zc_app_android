package com.tolstoy.zurichat.ui.fragments.networking;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.tolstoy.zurichat.models.User;
import com.tolstoy.zurichat.ui.fragments.model.RoomData;
import com.tolstoy.zurichat.ui.fragments.viewmodel.ChannelMessagesViewModel;

import centrifuge.Client;
import centrifuge.DisconnectEvent;
import centrifuge.DisconnectHandler;
import centrifuge.Subscription;

public class AppDisconnectHandler implements DisconnectHandler {
    protected Activity context;
    private final RoomData roomData;
    private final ChannelMessagesViewModel channelMessagesViewModel;
    private final User user;

    public AppDisconnectHandler(Context context, User user, RoomData room, ChannelMessagesViewModel channelMessagesViewModel) {
        this.context = (Activity) context;
        this.roomData = room;
        this.channelMessagesViewModel = channelMessagesViewModel;
        this.user = user;
    }

    @Override
    public void onDisconnect(Client client, final DisconnectEvent event) {
        context.runOnUiThread(() -> channelMessagesViewModel.isConnected(false));
    }
}
