package com.zurichat.app.ui.organizations.localdatabase.TypeConverters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zurichat.app.models.organization_model.OrgData;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class OrgDataTypeConverter {
    Gson gson = new Gson();

    @TypeConverter
    public List<OrgData> storedStringToDataList(String value) {
        if (value == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<OrgData>>() {}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public String dataListToStoredString(List<OrgData> dataList) {
        return gson.toJson(dataList);
    }
}
