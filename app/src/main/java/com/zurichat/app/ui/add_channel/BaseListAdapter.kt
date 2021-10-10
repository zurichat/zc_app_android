package com.zurichat.app.ui.add_channel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class BaseListAdapter(private val itemClickCallback: ((BaseItem<*>) -> Unit)?) : ListAdapter<BaseItem<*>, BaseViewHolder<*>>(

    AsyncDifferConfig.Builder(object : DiffUtil.ItemCallback<BaseItem<*>>() {
        override fun areItemsTheSame(oldItem: BaseItem<*>, newItem: BaseItem<*>): Boolean {
            return oldItem.uniqueId == newItem.uniqueId
        }

        override fun areContentsTheSame(oldItem: BaseItem<*>, newItem: BaseItem<*>): Boolean {
            return oldItem == newItem
        }
    }).build()

) {
    private var lastItemForViewTypeLookup: BaseItem<*>? = null

    override fun getItemViewType(position: Int) = getItem(position).layoutId

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {

        // The layoutId is used as the viewType
        val itemView = LayoutInflater.from(parent.context).inflate(viewType, parent, false)

        // Get the item so we can create the specific binding that the holder needs
        val item = getItemForViewType(viewType)
        return BaseViewHolder(item.initializeViewBinding(itemView))
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        getItem(position).bind(holder, itemClickCallback)
    }

    /**
     * Copied from Groupie library:
     *
     * This idea was copied from Epoxy. :wave: Bright idea guys!
     *
     *
     * Find the model that has the given view type so we can create a viewholder for that model.
     *
     *
     * To make this efficient, we rely on the RecyclerView implementation detail that [ListAdapter.getItemViewType]
     * is called immediately before [ListAdapter.onCreateViewHolder]. We cache the last model
     * that had its view type looked up, and unless that implementation changes we expect to have a
     * very fast lookup for the correct model.
     *
     *
     * To be safe, we fallback to searching through all models for a view type match. This is slow and
     * shouldn't be needed, but is a guard against RecyclerView behavior changing.
     */
    private fun getItemForViewType(viewType: Int): BaseItem<*> {
        val lastItemForViewTypeLookup = lastItemForViewTypeLookup
        if (lastItemForViewTypeLookup != null
            && lastItemForViewTypeLookup.layoutId == viewType
        ) {
            // We expect this to be a hit 100% of the time
            return lastItemForViewTypeLookup
        }

        // To be extra safe in case RecyclerView implementation details change...
        for (i in 0 until itemCount) {
            val item: BaseItem<*> = getItem(i)
            if (item.layoutId == viewType) {
                return item
            }
        }
        throw IllegalStateException("Could not find model for view type: $viewType")
    }
}