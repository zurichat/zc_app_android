package com.zurichat.app.ui.dm.audio

class AudioInfo {
    var Title: String? = null
    var Author: String? = null
    var SongUrl: String? = null
    var Size: Int? = null
    var Date: Int? = null

    constructor(Title: String?, Author: String?, SongUrl: String?, Size: Int?, Date: Int?) {
        this.Title = Title
        this.Author = Author
        this.SongUrl = SongUrl
        this.Size = Size
        this.Date = Date
    }

    constructor()


}