package com.tolstoy.zurichat.ui.fragments.networking;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.tolstoy.zurichat.models.User;
import com.tolstoy.zurichat.ui.fragments.model.RoomData;
import com.tolstoy.zurichat.ui.fragments.viewmodel.ChannelMessagesViewModel;
import com.tolstoy.zurichat.ui.fragments.viewmodel.ChannelViewModel;
import com.tolstoy.zurichat.ui.fragments.viewmodel.SharedChannelViewModel;

import centrifuge.Client;
import centrifuge.DisconnectEvent;
import centrifuge.DisconnectHandler;
import centrifuge.Subscription;

public class AppDisconnectHandler implements DisconnectHandler {
    protected Activity context;
    private RoomData roomData;
    private ChannelMessagesViewModel channelMessagesViewModel;
    private ChannelViewModel channelViewModel;
    private SharedChannelViewModel sharedChannelViewModel;
    private final User user;

    public AppDisconnectHandler(Context context, User user, RoomData room, ChannelMessagesViewModel channelMessagesViewModel) {
        this.context = (Activity) context;
        this.roomData = room;
        this.channelMessagesViewModel = channelMessagesViewModel;
        this.user = user;
    }

    public AppDisconnectHandler(Context context, User user, SharedChannelViewModel sharedChannelViewModel) {
        this.context = (Activity) context;
        this.sharedChannelViewModel = sharedChannelViewModel;
        this.user = user;
    }

    public AppDisconnectHandler(Context context, User user, ChannelViewModel channelViewModel) {
        this.context = (Activity) context;
        this.channelViewModel = channelViewModel;
        this.user = user;
    }

    @Override
    public void onDisconnect(Client client, final DisconnectEvent event) {
        context.runOnUiThread(() -> {
            if (channelMessagesViewModel!=null){
                channelMessagesViewModel.isConnected(false);
            }

            if (channelViewModel!=null){
                channelViewModel.isConnected(false);
            }
        });
    }
}
