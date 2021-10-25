package com.zurichat.app.models.network_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 22-Sep-21
 */

data class FileUploadResponse(
    @SerializedName("status")
    @Expose
    val status: Int = 0, // 200
    @SerializedName("message")
    @Expose
    val message: String = "", // File Upload Successful
    @SerializedName("data")
    @Expose
    val `data`: FileUploadResponseData = FileUploadResponseData()
){
    data class FileUploadResponseData(
        @SerializedName("file_url")
        @Expose
        val fileUrl: String = "", // https://api.zuri.chat/files/6135f65de2358b02686503a7/20210922220659_0.jpg
        @SerializedName("status")
        @Expose
        val status: Boolean = false // true
    )
}