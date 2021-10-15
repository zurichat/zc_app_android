package com.zurichat.app.ui.organizations.localdatabase.TypeConverters

import androidx.room.TypeConverter
import com.google.gson.Gson

class StringListTypeConverter {
    @TypeConverter
    fun imagesToJson(images:List<String>): String = Gson().toJson(images)

    @TypeConverter
    fun jsonToImages(value: String) = Gson().fromJson(value, Array<String>::class.java).toList()
}