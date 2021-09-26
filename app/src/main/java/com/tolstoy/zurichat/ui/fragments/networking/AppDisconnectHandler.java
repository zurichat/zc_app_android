package com.tolstoy.zurichat.ui.fragments.networking;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import centrifuge.Client;
import centrifuge.DisconnectEvent;
import centrifuge.DisconnectHandler;

public class AppDisconnectHandler implements DisconnectHandler {
    protected Activity context;

    public AppDisconnectHandler(Context context) {
        this.context = (Activity) context;
    }

    @Override
    public void onDisconnect(Client client, final DisconnectEvent event) {
        context.runOnUiThread(() -> Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show());
    }
}
