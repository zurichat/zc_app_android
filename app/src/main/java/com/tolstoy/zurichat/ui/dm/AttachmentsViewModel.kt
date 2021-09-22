package com.tolstoy.zurichat.ui.dm

import android.content.Context
import android.content.UriMatcher
import android.content.UriPermission
import android.net.Uri
import android.os.Environment
import android.provider.BaseColumns
import android.provider.DocumentsContract
import android.provider.DocumentsProvider
import android.provider.MediaStore
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.http.Url


/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 15-Sep-21
 */
class AttachmentsViewModel: ViewModel() {

    private val images = MutableLiveData<List<Uri>>()
    private val videos = MutableLiveData<List<Uri>>()
    private val documents = MutableLiveData<List<Uri>>()
    private val audio = MutableLiveData<List<Uri>>()

    fun getImages(context: Context) =
        get(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            MediaStore.Images.Media.INTERNAL_CONTENT_URI, images
        )

    fun getVideos(context: Context) =
        get(
            context, MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            MediaStore.Video.Media.INTERNAL_CONTENT_URI, videos
        )

    fun getAudio(context: Context) =
        get(
            context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            MediaStore.Audio.Media.INTERNAL_CONTENT_URI, audio
        )

    fun getDoc(context: Context) =
        get(
            context, DocumentsContract.buildChildDocumentsUri(null, null),
            DocumentsContract.buildSearchDocumentsUri(null, null, null), documents
        )


    private fun get(
        context: Context, fromExternal: Uri, fromInternal: Uri,
        liveData: MutableLiveData<List<Uri>>
    ): LiveData<List<Uri>> {

        viewModelScope.launch(Dispatchers.IO) {
            // checks if external storage is available on the users device
            liveData.postValue(
                if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)
                    load(context, fromExternal)
                else load(context, fromInternal)
            )
        }
        return liveData
    }

    private fun load(context: Context, from: Uri): List<Uri>{
        val columnIndexID: Int
        val path = mutableListOf<Uri>()
        val projection = arrayOf(BaseColumns._ID)
        var itemId: Long
        context.contentResolver.query(from, projection, null, null, null)?.also{
            columnIndexID = it.getColumnIndexOrThrow(BaseColumns._ID)
            while (it.moveToNext()) {
                itemId = it.getLong(columnIndexID)
                val uriImage = Uri.withAppendedPath(from, itemId.toString())
                path.add(uriImage)
            }
            it.close()
        }
        return path
    }
}