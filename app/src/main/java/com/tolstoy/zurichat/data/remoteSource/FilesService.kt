package com.tolstoy.zurichat.data.remoteSource

import com.tolstoy.zurichat.models.network_response.FileUploadResponse
import okhttp3.MultipartBody
import retrofit2.http.*

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 18-Sep-21
 */
interface FilesService {

    @Multipart
    @POST("file/{plugin_id}")
    suspend fun uploadFile(
        @Header("Authorization") authToken: String?,
        @Path("plugin_id") pluginId: String = KEY,
        @Part file: MultipartBody.Part
    ): FileUploadResponse

    companion object {
        const val BASE_URL = "https://api.zuri.chat/upload/"
        private const val KEY = "6135f65de2358b02686503a7"
    }
}