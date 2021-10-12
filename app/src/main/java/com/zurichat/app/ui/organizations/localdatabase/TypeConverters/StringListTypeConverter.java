package com.zurichat.app.ui.organizations.localdatabase.TypeConverters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zurichat.app.ui.fragments.model.Data;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class StringListTypeConverter {
    Gson gson = new Gson();

    @TypeConverter
    public List<String> storedStringToStringList(String value) {
        if (value == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<Data>>() {}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public String stringListToStoredString(List<String> emojiList) {
        return gson.toJson(emojiList);
    }
}
