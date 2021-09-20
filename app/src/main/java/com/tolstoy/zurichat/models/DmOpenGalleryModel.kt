package com.tolstoy.zurichat.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class DmOpenGalleryModel(
    @DrawableRes val ImageResource: Int,
    @StringRes val text: Int,
)
