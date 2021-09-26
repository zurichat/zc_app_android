package com.tolstoy.zurichat.ui.fragments.networking;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.tolstoy.zurichat.models.ChannelModel;
import com.tolstoy.zurichat.ui.fragments.model.RoomData;

import centrifuge.Client;
import centrifuge.ConnectEvent;
import centrifuge.ConnectHandler;
import centrifuge.ErrorEvent;
import centrifuge.ErrorHandler;
import centrifuge.SubscribeErrorEvent;
import centrifuge.SubscribeErrorHandler;
import centrifuge.SubscribeSuccessEvent;
import centrifuge.SubscribeSuccessHandler;
import centrifuge.Subscription;

public class AppConnectHandler implements ConnectHandler {
    protected Activity context;
    private final RoomData roomData;

    public AppConnectHandler(Context context, RoomData room) {
        this.context = (Activity) context;
        this.roomData = room;
    }

    @Override
    public void onConnect(Client client, final ConnectEvent event) {
        context.runOnUiThread(() -> {
            Subscription sub;
            try {
                sub = client.newSubscription(roomData.getSocket_name());

                client.onError(new ErrorHandler() {
                    @Override
                    public void onError(Client client, ErrorEvent errorEvent) {
                        Log.i("Subscribe",errorEvent.getMessage());
                    }
                });

                sub.onSubscribeSuccess(new SubscribeSuccessHandler() {
                    @Override
                    public void onSubscribeSuccess(Subscription subscription, SubscribeSuccessEvent subscribeSuccessEvent) {
                        Log.i("Subscribe","Success");
                    }
                });

                sub.onSubscribeError(new SubscribeErrorHandler() {
                    @Override
                    public void onSubscribeError(Subscription subscription, SubscribeErrorEvent subscribeErrorEvent) {
                        Toast.makeText(context, "Subscribed Error", Toast.LENGTH_SHORT).show();
                        Log.i("Subscribe","Not Successful");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }
}
