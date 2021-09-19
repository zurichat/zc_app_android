package com.tolstoy.zurichat.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import com.tolstoy.zurichat.data.remoteSource.ImageService.Companion.KEY
import com.tolstoy.zurichat.data.remoteSource.ImageService.Companion.imageService
import com.tolstoy.zurichat.models.network_response.ImageUploadResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody

import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream


/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 19-Sep-21
 */
class ImagesRepository {

    suspend fun uploadImage(context: Context, uri: Uri): ImageUploadResponse{
        return withContext(Dispatchers.IO){
            val file = File(context.externalCacheDir, "image").apply {
                if(!exists()) createNewFile()
                val stream = FileOutputStream(this)
                stream.write(context.contentResolver.openInputStream(uri)?.readBytes())
            }
            val request = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val multipart = MultipartBody.Builder()
                .addFormDataPart("image",uri.toString(),request).build()
            return@withContext imageService.uploadImage(KEY, multipart.part(0))
        }
    }

    companion object {
        val INSTANCE by lazy { ImagesRepository() }
    }
}