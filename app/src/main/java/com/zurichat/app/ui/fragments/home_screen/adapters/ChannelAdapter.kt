package com.zurichat.app.ui.fragments.home_screen.adapters

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import centrifuge.PublishEvent
import centrifuge.Subscription
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.zurichat.app.R
import com.zurichat.app.data.localSource.AppDatabase
import com.zurichat.app.models.Channel
import com.zurichat.app.models.ChannelModel
import com.zurichat.app.ui.fragments.channel_chat.localdatabase.RoomDao
import com.zurichat.app.ui.fragments.channel_chat.localdatabase.RoomDataObject
import com.zurichat.app.ui.fragments.home_screen.CentrifugeClient
import com.zurichat.app.ui.fragments.home_screen.CentrifugeClient.CustomListener
import com.zurichat.app.ui.fragments.model.Data
import com.zurichat.app.ui.fragments.networking.JoinNewChannel
import com.zurichat.app.ui.fragments.networking.RetrofitClientInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.charset.StandardCharsets

class ChannelAdapter(val context: Activity, private val list: List<ChannelModel>,val uiScope: CoroutineScope,val roomDao: RoomDao) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var onItemClickListener: ((channel:ChannelModel) -> Unit)? = null
    private var onAddChannelClickListener: (() -> Unit)? = null
    lateinit var organizationId : String
    lateinit var database: AppDatabase

    fun setItemClickListener(listener: (channel:ChannelModel) -> Unit) {
        onItemClickListener = listener
    }

    fun setAddChannelClickListener(listener: () -> Unit) {
        onAddChannelClickListener = listener
    }

    inner class ChannelViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(channel: ChannelModel) {
            val fab = view.findViewById<FloatingActionButton>(R.id.fab)
            val badge = view.findViewById<MaterialButton>(R.id.badge)
            badge.visibility = View.GONE

            if (!channel.isPrivate){
                fab.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_hash))
            }else{
                fab.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_new_lock))
            }

            view.findViewById<TextView>(R.id.channelTitle).text = channel.name
            view.findViewById<ConstraintLayout>(R.id.root_layout).setOnClickListener {
                onItemClickListener?.invoke(channel)
            }

            var count = 0
            uiScope.launch(Dispatchers.IO){
                roomDao.getRoomDataWithChannelID(channel._id).let { roomDataObject ->
                   /* if (roomDataObject!=null){
                        try{
                            CentrifugeClient.setCustomListener(object : CustomListener {
                                override fun onConnected(connected: Boolean) {
                                    try{
                                        CentrifugeClient.subscribeToChannel(roomDataObject.socketName)
                                        uiScope.launch(Dispatchers.Main){
                                            Toast.makeText(context,""+roomDataObject.socketName,Toast.LENGTH_SHORT).show()
                                        }
                                    }catch (e : Exception){
                                        e.printStackTrace()
                                    }
                                }
                                override fun onDataPublished(subscription: Subscription?, publishEvent: PublishEvent?) {
                                    uiScope.launch(Dispatchers.Main){
                                        val dataString = String(publishEvent!!.data, StandardCharsets.UTF_8)
                                        val data = Gson().fromJson(dataString, Data::class.java)
                                        if (data.channel_id == channel._id) {
                                            count++
                                            badge.text = count.toString()
                                        }
                                    }
                                }
                            })
                        }catch(e : Exception){
                            e.printStackTrace()
                        }
                    }else{
                        val room = RetrofitClientInstance.retrofitInstance?.create(JoinNewChannel::class.java)?.getRoom(organizationId,channel._id)
                        room?.let {
                            try{
                                CentrifugeClient.setCustomListener(object : CustomListener {
                                    override fun onConnected(connected: Boolean) {
                                        try{
                                            CentrifugeClient.subscribeToChannel(it.socket_name)
                                            uiScope.launch(Dispatchers.Main){
                                                Toast.makeText(context,""+it.socket_name,Toast.LENGTH_SHORT).show()
                                            }
                                        }catch (e : Exception){
                                            e.printStackTrace()
                                        }
                                    }
                                    override fun onDataPublished(subscription: Subscription?, publishEvent: PublishEvent?) {
                                        uiScope.launch(Dispatchers.Main){
                                            val dataString = String(publishEvent!!.data, StandardCharsets.UTF_8)
                                            val data = Gson().fromJson(dataString, Data::class.java)
                                            if (data.channel_id == channel._id) {
                                                count++
                                                badge.text = count.toString()
                                            }
                                        }
                                    }
                                })

                                val roomDataObject1 = RoomDataObject()
                                roomDataObject1.channelId = it.channel_id
                                roomDataObject1.socketName = it.socket_name

                                roomDao.insertAll(roomDataObject1)
                            }catch(e : Exception){
                                e.printStackTrace()
                            }
                        }
                    }*/
                }
            }
        }
    }

    inner class HeaderViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(channel: ChannelModel) {
            val nameView = view.findViewById<TextView>(R.id.headerTextView)
            nameView.text = channel.name

            /***
             * Checks if the header is a Unread Message Header Or Add New Channel Header.
             * Removes The Add Icon If it is a Unread Message Header
             * Sets A Click Listener If It is a Add Channel Header
             */
            if (channel.type == "channel_header_add"){
                view.findViewById<ConstraintLayout>(R.id.root_layout).setOnClickListener {
                    onAddChannelClickListener?.invoke()
                }
            }else{
                nameView.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0)
            }
        }
    }

    inner class DividerViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(channel: Channel) {
            val nameView = view.findViewById<View>(R.id.divider)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        val view : View
        val viewHolder: RecyclerView.ViewHolder

        if (viewType == 0){
            view = context.layoutInflater.inflate(R.layout.channel_header_model, parent, false)
            viewHolder = HeaderViewHolder(view)
            return viewHolder
        }else if (viewType == 2){
            view = context.layoutInflater.inflate(R.layout.channel_divider_model, parent, false)
            viewHolder = DividerViewHolder(view)
            return viewHolder
        }
        view = context.layoutInflater.inflate(R.layout.channels_list_item, parent, false)
        viewHolder = ChannelViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ChannelViewHolder) {
            holder.bind(list[position])
            //Perform Channel Related Actions Here
        }else if (holder is HeaderViewHolder){
            holder.bind(list[position])
            holder.setIsRecyclable(false)
            //Perform Header Related actions here
        }
    }

    override fun getItemCount() = list.size

    override fun getItemId(position: Int): Long {
        return list[position].hashCode().toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }

}