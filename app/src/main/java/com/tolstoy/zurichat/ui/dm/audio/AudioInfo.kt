package com.tolstoy.zurichat.ui.dm.audio

import android.net.Uri

class AudioInfo {
    var Title: String? = null
    var Author: String? = null
    var SongUrl: String? = null
    var Size: Int? = null
    var Date: Int? = null
    var selected: Boolean = false

    constructor()
    constructor(
        Title: String?,
        Author: String?,
        SongUrl: String?,
        Size: Int?,
        Date: Int?,
        selected: Boolean,
    ) {
        this.Title = Title
        this.Author = Author
        this.SongUrl = SongUrl
        this.Size = Size
        this.Date = Date
        this.selected = selected
        this.choose = choose
    }


}