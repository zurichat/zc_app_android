package com.tolstoy.zurichat.ui.fragments.home_screen.diff_utils

import androidx.recyclerview.widget.DiffUtil
import com.tolstoy.zurichat.models.Channel
import java.util.ArrayList

/****
 * This Class Handles All Changes In The Channels Recycler View Adapter and Helps to Dispatch The Correct Animations
 */
internal class ChannelDiffUtil(private val formerItems: ArrayList<Channel>, private val newItems: ArrayList<Channel>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return formerItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return formerItems[oldItemPosition].id == newItems[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return formerItems[oldItemPosition].id == newItems[newItemPosition].id
    }

}