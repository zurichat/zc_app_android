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
import io.github.centrifugal.centrifuge.ErrorEvent;
import io.github.centrifugal.centrifuge.EventListener;
import io.github.centrifugal.centrifuge.Options;
import io.github.centrifugal.centrifuge.PublishEvent;
import io.github.centrifugal.centrifuge.SubscribeErrorEvent;
import io.github.centrifugal.centrifuge.SubscribeSuccessEvent;
import io.github.centrifugal.centrifuge.Subscription;
import io.github.centrifugal.centrifuge.SubscriptionEventListener;
import io.github.centrifugal.centrifuge.UnsubscribeEvent;


public class CentrifugeClient {
    private static Client client = null;
    private static ArrayList<String> channelRoomIDList;
    private static ArrayMap<String, Subscription> subscriptionArrayMap;
    private static boolean connected = false;
    private static ChannelListener channelListener;

    private static ArrayList<String> dmRoomIDList;
    private static ArrayMap<String, Subscription> dmSubscriptionArrayMap;
    private static ChannelListener dmListener;

    public static synchronized Client getClient(User user) throws JSONException {
        if (client==null) {
            channelRoomIDList = new ArrayList<>();
            subscriptionArrayMap = new ArrayMap<>();

            dmRoomIDList = new ArrayList<>();
            dmSubscriptionArrayMap = new ArrayMap<>();

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

                @Override
                public void onError(Client client, ErrorEvent event) {
                    super.onError(client, event);
                    channelListener.onConnectError(client, event);
                }
            };
            client = new Client("wss://realtime.zuri.chat/connection/websocket?format=protobuf", new Options(), listener);
            client.setConnectData(bytes);
            client.connect();
        }else {
            if (!isConnected()){
                client.connect();
            }
        }
        return client;
    }

    public static boolean isConnected(){
        return connected;
    }

    public static void subscribeToChannel(String channelRoomID) {
        if(!channelRoomIDList.contains(channelRoomID) && isConnected()){
            SubscriptionEventListener subListener = new SubscriptionEventListener() {
                @Override
                public void onSubscribeSuccess(Subscription sub, SubscribeSuccessEvent event) {
                    channelRoomIDList.add(channelRoomID);
                    subscriptionArrayMap.put(channelRoomID,sub);
                    channelListener.onChannelSubscribed(true,sub);
                }

                @Override
                public void onSubscribeError(Subscription sub, SubscribeErrorEvent event) {
                    channelListener.onChannelSubscriptionError(sub, event);
                }

                @Override
                public void onPublish(Subscription sub, PublishEvent event) {
                    channelListener.onDataPublished(sub,event);
                }

                @Override
                public void onUnsubscribe(Subscription sub, UnsubscribeEvent event) {
                    super.onUnsubscribe(sub, event);
                    client.removeSubscription(sub);
                    channelListener.onChannelSubscribed(false,sub);
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

    public static Subscription getChannelSubscription(String roomID){
        return subscriptionArrayMap.get(roomID);
    }

    public static void subscribeToDm(String channelRoomID) {
        if(!dmRoomIDList.contains(channelRoomID) && isConnected()){
            SubscriptionEventListener subListener = new SubscriptionEventListener() {
                @Override
                public void onSubscribeSuccess(Subscription sub, SubscribeSuccessEvent event) {
                    dmRoomIDList.add(channelRoomID);
                    dmSubscriptionArrayMap.put(channelRoomID,sub);
                    dmListener.onChannelSubscribed(true,sub);
                }

                @Override
                public void onSubscribeError(Subscription sub, SubscribeErrorEvent event) {
                    dmListener.onChannelSubscriptionError(sub, event);
                }

                @Override
                public void onPublish(Subscription sub, PublishEvent event) {
                    dmListener.onDataPublished(sub,event);
                }

                @Override
                public void onUnsubscribe(Subscription sub, UnsubscribeEvent event) {
                    super.onUnsubscribe(sub, event);
                    client.removeSubscription(sub);
                    dmListener.onChannelSubscribed(false,sub);
                }
            };
            Subscription subscription = client.newSubscription(channelRoomID,subListener);
            subscription.subscribe();
        }
    }

    public static void unSubscribeFromDm(String channelRoomID) {
        if (dmRoomIDList.contains(channelRoomID)){
            Subscription subscription = dmSubscriptionArrayMap.get(channelRoomID);
            if (subscription!=null){
                subscription.unsubscribe();
            }
        }
    }

    public static Subscription getDmSubscription(String roomID){
        return dmSubscriptionArrayMap.get(roomID);
    }

    public interface ChannelListener {
        void onConnected(boolean connected);
        void onConnectError(Client client, ErrorEvent event);
        void onDataPublished(Subscription subscription, PublishEvent publishEvent);
        void onChannelSubscribed(boolean isSubscribed, Subscription subscription);
        void onChannelSubscriptionError(Subscription subscription, SubscribeErrorEvent event);
    }

    public static void setCustomListener(ChannelListener listener){
        channelListener = listener;
    }

    public static void setDmCustomListener(ChannelListener listener){
        dmListener = listener;
    }
}
