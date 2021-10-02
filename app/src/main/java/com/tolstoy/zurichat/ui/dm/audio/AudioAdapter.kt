package com.tolstoy.zurichat.ui.dm.audio

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.ItemAttachmentAudioBinding
import com.tolstoy.zurichat.ui.dm.MEDIA
import com.tolstoy.zurichat.ui.dm.adapters.AttachmentViewHolder
import com.tolstoy.zurichat.ui.dm.audio.AudioAdapter.AudioViewHolder
import com.tolstoy.zurichat.util.changeVisibility

class AudioAdapter(
    private val context: Context,
    private var audioList: ArrayList<AudioInfo>,
    private val media: MEDIA
) : RecyclerView.Adapter<AudioViewHolder>() {

    companion object {
        val MUSICITEMPOS = "MUSICTEMPOS"
        val MUSICLIST = "MUSICLIST"
    }

    override fun getItemViewType(position: Int) = media.ordinal

    class AudioViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var name = itemView.findViewById<TextView>(R.id.audio_title)
        var author = itemView.findViewById<TextView>(R.id.author_txt)
        var size = itemView.findViewById<TextView>(R.id.size_txt_audio)
        var date = itemView.findViewById<TextView>(R.id.date_txt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_attachment_audio, parent, false)
        val attach = AudioViewHolder(view)
        return when (viewType) {
            MEDIA.AUDIO.ordinal -> attach
            else -> attach
        }
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        var audioCurrent = audioList[position]
        holder.name.text = audioCurrent.Title
        holder.author.text = audioCurrent.Author
        holder.date.text = audioCurrent.Date.toString()
        holder.size.text = audioCurrent.Size.toString()
    }

    override fun getItemCount(): Int {
        return audioList.size
    }
}