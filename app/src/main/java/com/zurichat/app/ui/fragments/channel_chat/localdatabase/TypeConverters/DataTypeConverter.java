package com.zurichat.app.ui.fragments.channel_chat.localdatabase.TypeConverters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zurichat.app.ui.fragments.model.Data;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class DataTypeConverter {
    Gson gson = new Gson();

    @TypeConverter
    public List<Data> storedStringToEmojis(String value) {
        if (value == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<Data>>() {}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public String emojisToStoredString(List<Data> emojiList) {
        return gson.toJson(emojiList);
    }
}
