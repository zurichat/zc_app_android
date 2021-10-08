package com.zurichat.app.ui.fragments.home_screen;

import androidx.collection.ArrayMap;

import com.zurichat.app.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import io.github.centrifugal.centrifuge.Client;
import io.github.centrifugal.centrifuge.ConnectEvent;
import io.github.centrifugal.centrifuge.DisconnectEvent;
import io.github.centrifugal.centrifuge.EventListener;
import io.github.centrifugal.centrifuge.Options;
import io.github.centrifugal.centrifuge.PublishEvent;
import io.github.centrifugal.centrifuge.SubscribeErrorEvent;
import io.github.centrifugal.centrifuge.SubscribeSuccessEvent;
import io.github.centrifugal.centrifuge.Subscription;
import io.github.centrifugal.centrifuge.SubscriptionEventListener;
import io.github.centrifugal.centrifuge.UnsubscribeEvent;
import timber.log.Timber;

public class CentrifugeClient {
    private static Client client = null;
    private static ArrayList<String> channelRoomIDList;
    private static ArrayMap<String, Subscription> subscriptionArrayMap;
    private static boolean connected = false;
    private static ChannelListener channelListener;

    public static synchronized Client getClient(User user) throws JSONException {
        if (client==null) {
            channelRoomIDList = new ArrayList<>();
            subscriptionArrayMap = new ArrayMap<>();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("bearer",user.getToken());

            byte[] bytes = jsonObject.toString().getBytes(StandardCharsets.UTF_8);

            EventListener listener = new EventListener() {
                @Override
                public void onConnect(Client client, ConnectEvent event) {
                    connected = true;
                    channelListener.onConnected(true);
                }

                @Override
                public void onDisconnect(Client client, DisconnectEvent event) {
                    connected = false;
                    channelListener.onConnected(false);
                }
            };
            client = new Client(
                    "wss://realtime.zuri.chat/connection/websocket?format=protobuf",
                    new Options(),
                    listener
            );
            client.setConnectData(bytes);
            client.connect();
        }
        return client;
    }

    public static void subscribeToChannel(String channelRoomID) {
        if(!channelRoomIDList.contains(channelRoomID) && isConnected()){
            SubscriptionEventListener subListener = new SubscriptionEventListener() {
                @Override
                public void onSubscribeSuccess(Subscription sub, SubscribeSuccessEvent event) {
                    channelRoomIDList.add(channelRoomID);
                    subscriptionArrayMap.put(channelRoomID,sub);
                }

                @Override
                public void onSubscribeError(Subscription sub, SubscribeErrorEvent event) {
                    Timber.e("Subscribed%s", event.getMessage());
                }

                @Override
                public void onPublish(Subscription sub, PublishEvent event) {
                    channelListener.onDataPublished(sub,event);
                }

                @Override
                public void onUnsubscribe(Subscription sub, UnsubscribeEvent event) {
                    super.onUnsubscribe(sub, event);
                    client.removeSubscription(sub);
                }
            };
            Subscription subscription = client.newSubscription(channelRoomID,subListener);
            subscription.subscribe();
        }
    }

    public static void unSubscribeFromChannel(String channelRoomID) {
        if (channelRoomIDList.contains(channelRoomID)){
            Subscription subscription = subscriptionArrayMap.get(channelRoomID);
            if (subscription!=null){
                subscription.unsubscribe();
            }
        }
    }

    public static boolean isConnected(){
        return connected;
    }

    public interface ChannelListener {
        void onConnected(boolean connected);
        void onDataPublished(Subscription subscription, PublishEvent publishEvent);
    }

    public static void setCustomListener(ChannelListener listener){
        channelListener = listener;
    }

}
