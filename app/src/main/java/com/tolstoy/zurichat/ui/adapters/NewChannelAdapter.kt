package com.tolstoy.zurichat.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tolstoy.zurichat.databinding.NewChannelItemBinding
import com.tolstoy.zurichat.models.User
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.ui.dm.DMFragment


class NewChannelAdapter(val fragment: Fragment): RecyclerView.Adapter<NewChannelAdapter.ViewHolder>(), Filterable {
    var list = emptyList<User>()
    val _list: List<User> by lazy { list }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            NewChannelItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(private var item: NewChannelItemBinding) : RecyclerView.ViewHolder(item.root) {
        fun bind(chat: User) {
            item.channelItemPersonNameTxt.text =
                if(chat.first_name.isEmpty() && chat.last_name.isEmpty()) "No name"
                else "${chat.first_name} ${chat.last_name}"


            item.channelItemPersonIcon.setBackgroundResource(R.drawable.ic_kolade_icon)
            item.channelItemMessageTxt.text = chat.email
            item.root.setOnClickListener {
                // will crash the app because no value is being passed
                it.findNavController().navigate(R.id.dmFragment)
            }

        }
    }

    override fun getFilter(): Filter {
        return _filter
    }

    val _filter = object : Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterList = mutableListOf<User>()

            for(i in _list) {
                if("${i.first_name}${i.last_name}".contains(constraint?:"",true)) {
                    filterList.add(i)
                }
            }

            if(filterList.isEmpty()) {
                for(i in _list) {
                    if(i.email.contains(constraint?:"",true)) {
                        filterList.add(i)
                    }
                }
            }
            return FilterResults().apply {
                values = filterList
            }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            val resultList = results?.values as MutableList<User>

            if (resultList.isEmpty()) {
                list = _list
                notifyDataSetChanged()
            } else {
                list = results.values as MutableList<User>
                notifyDataSetChanged()
            }

        }

    }
}
