package com.zurichat.app.ui.fragments.channel_chat.localdatabase.TypeConverters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zurichat.app.ui.fragments.model.Emoji;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class EmojiTypeConverter {
    Gson gson = new Gson();

    @TypeConverter
    public List<Emoji> storedStringToEmojis(String value) {
        if (value == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<Emoji>>() {}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public String emojisToStoredString(List<Emoji> emojiList) {
        return gson.toJson(emojiList);
    }

}
