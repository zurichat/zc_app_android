package com.zurichat.app.ui.fragments.channel_chat.localdatabase.TypeConverters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.zurichat.app.ui.fragments.model.Event;

public class EventTypeConverter {
    Gson gson = new Gson();

    @TypeConverter
    public Event storedStringToEmojis(String value) {
        if (value == null) {
            return null;
        }
        return gson.fromJson(value, Event.class);
    }

    @TypeConverter
    public String emojisToStoredString(Event event) {
        return gson.toJson(event,Event.class);
    }
}
