package com.zurichat.app.data.dmGallery

import com.zurichat.app.R
import com.zurichat.app.models.DmOpenGalleryModel

class GalleryDataSource {
    fun loadGallery(): List<DmOpenGalleryModel> {
        return listOf<DmOpenGalleryModel>(
            DmOpenGalleryModel(R.drawable.all_images, R.string.image1),
            DmOpenGalleryModel(R.drawable.image2, R.string.image2),
            DmOpenGalleryModel(R.drawable.image3, R.string.image3),
            DmOpenGalleryModel(R.drawable.image4, R.string.image4),
            DmOpenGalleryModel(R.drawable.image5, R.string.image5),
            DmOpenGalleryModel(R.drawable.image6, R.string.image6),
            DmOpenGalleryModel(R.drawable.image7, R.string.image7),
            DmOpenGalleryModel(R.drawable.image8, R.string.image8)

        )
    }
}
