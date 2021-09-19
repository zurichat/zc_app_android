package com.tolstoy.zurichat.models.network_response


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class ImageUploadResponse(
    @SerializedName("data")
    @Expose
    val `data`: ImageUploadResponseData,
    @SerializedName("status")
    @Expose
    val status: Int, // 200
    @SerializedName("success")
    @Expose
    val success: Boolean // true
)