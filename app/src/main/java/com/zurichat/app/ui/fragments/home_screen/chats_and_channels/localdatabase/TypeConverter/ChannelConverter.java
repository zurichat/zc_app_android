package com.zurichat.app.ui.fragments.home_screen.chats_and_channels.localdatabase.TypeConverter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class ChannelConverter {
    Gson gson = new Gson();

    @TypeConverter
    public List<Object> storedStringToChannelList(String value) {
        if (value == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<Object>>() {}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public String channelListToString(List<Object> emojiList) {
        return gson.toJson(emojiList);
    }
}
