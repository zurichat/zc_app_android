package com.tolstoy.zurichat.ui.add_channel

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class BaseViewHolder<T : ViewBinding>(val binding: T,) : RecyclerView.ViewHolder(binding.root)