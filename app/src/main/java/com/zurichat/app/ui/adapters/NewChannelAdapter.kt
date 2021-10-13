
package com.zurichat.app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.zurichat.app.databinding.NewChannelItemBinding
import com.zurichat.app.models.OrganizationMember


class NewChannelAdapter(val fragment: Fragment): RecyclerView.Adapter<NewChannelAdapter.ViewHolder>(), Filterable {
    var list = emptyList<OrganizationMember>()
    val _list: List<OrganizationMember> by lazy { list }

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
        fun bind(chat: OrganizationMember) {
            item.channelItemPersonNameTxt.text =
                if(chat.firstName.isEmpty() && chat.lastName.isEmpty()) "No name"
                else "${chat.firstName} ${chat.lastName}"

           // item.channelItemPersonIcon.setBackgroundResource(R.drawable.ic_kolade_icon)
            item.channelItemMessageTxt.text = chat.email
            item.root.setOnClickListener {
                //will crash the app because no value is being passed
                //it.findNavController().navigate(R.id.dmFragment)
            }
        }
    }

    override fun getFilter(): Filter {
        return _filter
    }

    val _filter = object : Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterList = mutableListOf<OrganizationMember>()

            for(i in _list) {
                if("${i.firstName}${i.lastName}".contains(constraint?:"",true)) {
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
            val resultList = results?.values as MutableList<OrganizationMember>

            if (resultList.isEmpty()) {
                list = _list
                notifyDataSetChanged()
            } else {
                list = results.values as MutableList<OrganizationMember>
                notifyDataSetChanged()
            }

        }

    }
}
