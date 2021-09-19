package com.tolstoy.zurichat.models.network_response


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Data(
    @SerializedName("additionalProp1")
    @Expose
    val additionalProp1: String, // string
    @SerializedName("additionalProp2")
    @Expose
    val additionalProp2: String, // string
    @SerializedName("additionalProp3")
    @Expose
    val additionalProp3: String // string
)