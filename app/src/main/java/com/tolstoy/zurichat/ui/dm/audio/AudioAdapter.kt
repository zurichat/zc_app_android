package com.tolstoy.zurichat.ui.dm.audio

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.ActivityCameraBinding.bind
import com.tolstoy.zurichat.databinding.ItemAttachmentAudioBinding
import com.tolstoy.zurichat.ui.dm.MEDIA
import com.tolstoy.zurichat.ui.dm.adapters.AttachmentViewHolder
import com.tolstoy.zurichat.ui.dm.audio.AudioAdapter.AudioViewHolder
import com.tolstoy.zurichat.util.changeVisibility
import java.util.concurrent.TimeUnit

class AudioAdapter(
    private val context: Context,
    private var audioList: List<AudioInfo>,
    private val media: MEDIA,
) : RecyclerView.Adapter<AudioViewHolder>() {

    override fun getItemViewType(position: Int) = media.ordinal
    private lateinit var mp: MediaPlayer
    private lateinit var audioService: PlayMusicService
    val allMusicList: ArrayList<String> = ArrayList()

    private val list: MutableList<AudioInfo> = audioList as MutableList<AudioInfo>
    private val _selected = mutableListOf<Uri>()
    val selected: List<Uri> get() = _selected

    private val sendDrawable by lazy {
        ResourcesCompat.getDrawable(context.resources, R.drawable.playbtn, null)
    }
    private val speakDrawable by lazy {
        ResourcesCompat.getDrawable(context.resources, R.drawable.ic_pause, null)
    }
    var isSelected = false

    companion object {
        val MUSICLIST = "musiclist"
        val MUSICITEMPOS = "pos"
    }


    class AudioViewHolder(itemView: View, val selected: MutableList<Uri>) :
        RecyclerView.ViewHolder(itemView) {

        var name = itemView.findViewById<TextView>(R.id.audio_title)
        var author = itemView.findViewById<TextView>(R.id.author_txt)
        var size = itemView.findViewById<TextView>(R.id.size_txt_audio)
        var date = itemView.findViewById<TextView>(R.id.date_txt)
        var click = itemView.findViewById<ConstraintLayout>(R.id.parent)
        var play = itemView.findViewById<ImageView>(R.id.play)
        var pause = itemView.findViewById<ImageView>(R.id.pause)

        var view4 = itemView.findViewById<LinearLayout>(R.id.view4)
        var tick = itemView.findViewById<ImageView>(R.id.tick)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_attachment_audio, parent, false)

        val attach = AudioViewHolder(view, _selected)
        return when (viewType) {
            MEDIA.AUDIO.ordinal -> attach
            else -> attach
        }
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        val audioCurrent = audioList[position]
        audioService = PlayMusicService()


        holder.play.setImageDrawable(sendDrawable)
        holder.name.text = audioCurrent.Title
        holder.author.text = audioCurrent.Author
        holder.date.text = audioCurrent.Date.toString()
        holder.size.text = audioCurrent.Size.toString()

        holder.itemView.setOnClickListener {
            if (audioCurrent.selected) {
                // add the uri to the selected list
//                    _selected.add(uri)
                changeVisibility(View.VISIBLE, holder.tick, holder.view4)
            } else {
//                    _selected.remove(uri)
                changeVisibility(View.GONE, holder.tick, holder.view4)
            }
            audioCurrent.selected = !audioCurrent.selected

        }

        holder.play.setOnClickListener {


//                for (element in audioList){
//                    element.SongUrl?.let { allMusicList.add(it) }
//                }
//                val musicDataIntent = Intent(context, PlayMusicService::class.java)
//                musicDataIntent.putStringArrayListExtra(MUSICLIST, allMusicList)
//                musicDataIntent.putExtra(MUSICITEMPOS, position)
//                context.startService(musicDataIntent)


        }

    }

    fun toMeandS(millis: Long): String {
        var duration = String.format(
            "%02d: %02d",
            TimeUnit.MICROSECONDS.toMinutes(millis),
            TimeUnit.MICROSECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MICROSECONDS.toMinutes(millis)
            )
        )
        return duration
    }

    override fun getItemCount(): Int {
        return audioList.size
    }
}