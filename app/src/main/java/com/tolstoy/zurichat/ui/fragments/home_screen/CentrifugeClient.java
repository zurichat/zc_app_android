package com.tolstoy.zurichat.ui.fragments.home_screen;

import android.util.Log;

import androidx.collection.ArrayMap;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.tolstoy.zurichat.ui.fragments.viewmodel.SharedChannelViewModel;

import java.util.ArrayList;

import centrifuge.Centrifuge;
import centrifuge.Client;
import centrifuge.PublishEvent;
import centrifuge.Subscription;

public class CentrifugeClient {
    private static Client client = null;
    private static ArrayList<String> channelRoomIDList;
    private static ArrayMap<String,Subscription> subscriptionArrayMap;
    private static boolean connected = false;
    private static CustomListener customListener;

    public static synchronized Client getClient(FragmentActivity activity) throws Exception {
        if (client==null) {
            client = Centrifuge.new_("wss://realtime.zuri.chat/connection/websocket", Centrifuge.defaultConfig());
            channelRoomIDList = new ArrayList<>();
            subscriptionArrayMap = new ArrayMap<>();

            client.onConnect((client, connectEvent) -> {
                connected = true;
                customListener.onConnected(true);
            });
            client.onDisconnect((client, disconnectEvent) -> {
                connected = false;
                customListener.onConnected(false);
            });
            client.connect();
        }
        return client;
    }

    public static void subscribeToChannel(String channelRoomID) throws Exception {
        if(!channelRoomIDList.contains(channelRoomID) && isConnected()){
            Subscription subscription = client.newSubscription(channelRoomID);
            subscription.onSubscribeSuccess((subscription1, subscribeSuccessEvent) -> {
                channelRoomIDList.add(channelRoomID);
                subscriptionArrayMap.put(channelRoomID,subscription);
            });
            subscription.onPublish((subscription12, publishEvent) -> customListener.onDataPublished(subscription12,publishEvent));
            subscription.subscribe();
        }else {
            Subscription subscription = subscriptionArrayMap.get(channelRoomID);
            assert subscription != null;
            subscription.onPublish((subscription12, publishEvent) -> customListener.onDataPublished(subscription12,publishEvent));
        }
    }

    public static void unSubscribeFromChannel(String channelRoomID) throws Exception {
        if (channelRoomIDList.contains(channelRoomID)){
            Subscription subscription = subscriptionArrayMap.get(channelRoomID);
            assert subscription != null;
            subscription.onUnsubscribe((subscription1, unsubscribeEvent) -> {
                channelRoomIDList.remove(channelRoomID);
                subscriptionArrayMap.remove(channelRoomID);
            });
            subscription.unsubscribe();
        }
    }

    public static boolean isConnected(){
        return connected;
    }

    public interface CustomListener{
        void onConnected(boolean connected);
        void onDataPublished(Subscription subscription, PublishEvent publishEvent);
    }

    public static void setCustomListener(CustomListener listener){
        customListener = listener;
    }
}
