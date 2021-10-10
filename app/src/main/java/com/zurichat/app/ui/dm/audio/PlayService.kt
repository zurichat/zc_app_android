package com.zurichat.app.ui.dm.audio

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import androidx.core.content.res.ResourcesCompat
import com.zurichat.app.R

class PlayService : Service() {

    private val sendDrawable by lazy {
        ResourcesCompat.getDrawable(this.resources, R.drawable.playbtn, null)
    }
    private val speakDrawable by lazy {
        ResourcesCompat.getDrawable(this.resources, R.drawable.ic_pause, null)
    }
    var currentPos: Int = 0
    var musicDataList: ArrayList<String> = ArrayList()
    var mp = MediaPlayer()
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        currentPos = intent!!.getIntExtra(AudioAdapter.MUSICITEMPOS, 0)
        musicDataList = intent.getStringArrayListExtra(AudioAdapter.MUSICLIST)!!
        mp.setDataSource(musicDataList[currentPos])

        if (mp.equals(null)) {
            mp.stop()
            mp.release()
        }

        if (mp.isPlaying) {
            mp.pause()
        } else {
            mp.prepare()
            mp.setOnPreparedListener {

                mp.start()

            }

        }




        return super.onStartCommand(intent, flags, startId)
    }
}
