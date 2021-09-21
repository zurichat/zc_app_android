package com.tolstoy.zurichat.data.remoteSource

import com.tolstoy.zurichat.models.network_response.ImageUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 18-Sep-21
 */
interface ImageService {

    @Multipart
    @POST("1/upload")
    suspend fun uploadImage(@Query("key") key: String,
                            @Part image: MultipartBody.Part): ImageUploadResponse

    companion object {
        const val BASE_URL = "https://api.imgbb.com/"
        const val KEY = "476fef34c98f79720b1ad9c13a2541da"

        val imageService by lazy {
            Retrofit.retrofit(BASE_URL).create(ImageService::class.java)
        }
    }
}