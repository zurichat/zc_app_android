package com.tolstoy.zurichat.models.network_response


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class ImageUploadResponseData(
    @SerializedName("display_url")
    @Expose
    val displayUrl: String, // https://i.ibb.co/98W13PY/c1f64245afb2.gif
    @SerializedName("expiration")
    @Expose
    val expiration: String, // 0
    @SerializedName("id")
    @Expose
    val id: String, // 2ndCYJK
    @SerializedName("size")
    @Expose
    val size: String, // 42
    @SerializedName("time")
    @Expose
    val time: String, // 1552042565
    @SerializedName("title")
    @Expose
    val title: String, // c1f64245afb2
    @SerializedName("url")
    @Expose
    val url: String, // https://i.ibb.co/w04Prt6/c1f64245afb2.gif
    @SerializedName("url_viewer")
    @Expose
    val urlViewer: String // https://ibb.co/2ndCYJK
)