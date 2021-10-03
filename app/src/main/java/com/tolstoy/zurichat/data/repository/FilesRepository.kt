package com.tolstoy.zurichat.data.repository

import android.content.Context
import android.net.Uri
import com.tolstoy.zurichat.data.localSource.Cache
import com.tolstoy.zurichat.data.remoteSource.FilesService
import com.tolstoy.zurichat.models.network_response.FileUploadResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody

import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 19-Sep-21
 */
class FilesRepository @Inject constructor(private val service: FilesService) {
    private val auth = Cache.map.getOrDefault("USER", "").toString()

    suspend fun uploadFile(context: Context, uri: Uri): FileUploadResponse {
        return withContext(Dispatchers.IO) {
            val file = File(context.externalCacheDir, "file").apply {
                if (!exists()) createNewFile()
                val stream = FileOutputStream(this)
                stream.write(context.contentResolver.openInputStream(uri)?.readBytes())
            }
            val request = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val multipart = MultipartBody.Builder()
                .addFormDataPart("image", uri.toString(), request).build()
            return@withContext service.uploadFile(auth, file = multipart.part(0))
        }
    }
}