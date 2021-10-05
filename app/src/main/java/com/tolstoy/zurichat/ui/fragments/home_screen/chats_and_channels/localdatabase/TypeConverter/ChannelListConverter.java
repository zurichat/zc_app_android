package com.tolstoy.zurichat.ui.fragments.home_screen.chats_and_channels.localdatabase.TypeConverter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tolstoy.zurichat.models.ChannelModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChannelListConverter {
    Gson gson = new Gson();

    @TypeConverter
    public List<ChannelModel> storedStringToChannelList(String value) {
        if (value == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<ChannelModel>>() {}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public String channelListToStoredString(List<ChannelModel> channelList) {
        return gson.toJson(channelList);
    }
}
