package com.tolstoy.zurichat.ui.fragments.networking;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.tolstoy.zurichat.R;
import com.tolstoy.zurichat.models.User;
import com.tolstoy.zurichat.ui.fragments.model.Data;
import com.tolstoy.zurichat.ui.fragments.viewmodel.ChannelMessagesViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import centrifuge.PublishEvent;
import centrifuge.PublishHandler;
import centrifuge.Subscription;

public class AppPublishHandler implements PublishHandler {
    protected Activity context;
    private final ChannelMessagesViewModel channelMessagesViewModel;
    private final User user;

    public AppPublishHandler(Context context, User user, ChannelMessagesViewModel channelMessagesViewModel) {
        this.context = (Activity) context;
        this.channelMessagesViewModel = channelMessagesViewModel;
        this.user = user;
    }

    @Override
    public void onPublish(final Subscription sub, final PublishEvent event) {
        String dataString = new String(event.getData(), StandardCharsets.UTF_8);
        try {
            //TODO: Use Gson for this
            JSONObject jsonObject = new JSONObject(dataString);
            String _id = jsonObject.getString("_id");
            String user_id = jsonObject.getString("user_id");
            String channel_id = jsonObject.getString("channel_id");
            String content = jsonObject.getString("content");
            String type = jsonObject.getString("type");
            String timestamp = jsonObject.getString("timestamp");
            //String action = jsonObject.getJSONObject("event").getString("create:message");

            boolean has_files = jsonObject.optBoolean("has_files");
            boolean pinned = jsonObject.optBoolean("has_files");
            boolean edited = jsonObject.optBoolean("has_files");
            boolean can_reply = jsonObject.optBoolean("has_files");

            int replies = jsonObject.getInt("replies");

            Data data = new Data(_id,can_reply,channel_id,content,edited,null,null,null,has_files,pinned,replies,timestamp,type,user_id);
            channelMessagesViewModel.receiveMessage(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
