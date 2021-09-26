package com.tolstoy.zurichat.ui.fragments.networking;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.tolstoy.zurichat.R;

import centrifuge.PublishEvent;
import centrifuge.PublishHandler;
import centrifuge.Subscription;

public class AppPublishHandler implements PublishHandler {
    protected Activity context;

    public AppPublishHandler(Context context) {
        this.context = (Activity) context;
    }

    @Override
    public void onPublish(final Subscription sub, final PublishEvent event) {
        context.runOnUiThread(() -> Toast.makeText(context, "Published", Toast.LENGTH_SHORT).show());
    }
}
