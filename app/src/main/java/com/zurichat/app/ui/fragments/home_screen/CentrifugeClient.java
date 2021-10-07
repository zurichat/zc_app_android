package com.zurichat.app.ui.fragments.home_screen;

import android.util.Log;

import androidx.collection.ArrayMap;
import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.zurichat.app.models.User;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import centrifuge.Centrifuge;
import centrifuge.Client;
import centrifuge.ErrorEvent;
import centrifuge.ErrorHandler;
import centrifuge.PublishEvent;
import centrifuge.SubscribeErrorEvent;
import centrifuge.SubscribeErrorHandler;
import centrifuge.Subscription;
import kotlin.text.Charsets;

public class CentrifugeClient {
    private static Client client = null;
    private static ArrayList<String> channelRoomIDList;
    private static ArrayMap<String,Subscription> subscriptionArrayMap;
    private static boolean connected = false;
    private static CustomListener customListener;
    private static User user;

    public static synchronized Client getClient(FragmentActivity activity,User user) throws Exception {
        CentrifugeClient.user = user;
        if (client==null) {
            channelRoomIDList = new ArrayList<>();
            subscriptionArrayMap = new ArrayMap<>();

            client = Centrifuge.new_("wss://realtime.zuri.chat/connection/websocket", Centrifuge.defaultConfig());

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("bearer",user.getToken());
            String dataString = "{\"bearer\":" + "\""+user.getToken()+"\"" + "}";

            byte[] bytes = dataString.getBytes(StandardCharsets.UTF_8);
            String str = new String(bytes, StandardCharsets.UTF_8);
            Log.i("Token String",""+str);
            client.setConnectData(bytes);

            client.onConnect((client, connectEvent) -> {
                connected = true;
                if (customListener!=null){
                    Log.e("Centrifugo Error","Connect Done");
                    customListener.onConnected(true);
                }
            });
            client.onDisconnect((client, disconnectEvent) -> {
                connected = false;
                if (customListener!=null){
                    Log.e("Centrifugo Error","Connect Abort");
                    customListener.onConnected(false);
                }
            });
            client.onError(new ErrorHandler() {
                @Override
                public void onError(Client client, ErrorEvent errorEvent) {
                    Log.e("Centrifugo Error",errorEvent.toString());
                }
            });
            Log.e("Centrifugo Error","Connect Start");
            client.connect();
            Log.e("Centrifugo Error","Connect End");
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
            subscription.onSubscribeError(new SubscribeErrorHandler() {
                @Override
                public void onSubscribeError(Subscription subscription, SubscribeErrorEvent subscribeErrorEvent) {
                    Log.i("Subscription Error",""+subscribeErrorEvent.toString());
                }
            });
            subscription.onPublish((subscription12, publishEvent) -> customListener.onDataPublished(subscription12,publishEvent));
            subscription.subscribe();
        }else {
            Subscription subscription = subscriptionArrayMap.get(channelRoomID);
            if (subscription!=null){
                subscription.onPublish((subscription12, publishEvent) -> customListener.onDataPublished(subscription12,publishEvent));
            }
        }
    }

    public static void unSubscribeFromChannel(String channelRoomID) throws Exception {
        if (channelRoomIDList.contains(channelRoomID)){
            Subscription subscription = subscriptionArrayMap.get(channelRoomID);
            if (subscription!=null){
                subscription.onUnsubscribe((subscription1, unsubscribeEvent) -> {
                    channelRoomIDList.remove(channelRoomID);
                    subscriptionArrayMap.remove(channelRoomID);
                });
                subscription.unsubscribe();
            }
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
