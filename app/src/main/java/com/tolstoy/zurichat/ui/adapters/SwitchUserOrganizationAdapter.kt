package com.tolstoy.zurichat.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tolstoy.zurichat.databinding.OrgListItemBinding
import com.tolstoy.zurichat.models.organization_model.Data

class SwitchUserOrganizationAdapter(
    private val organizations: List<Data>,
    val context: Context
) : RecyclerView.Adapter<SwitchUserOrganizationAdapter.ViewHolder>() {

    var onClickListener: ((data:Data) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            OrgListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(organizations[position])
    }

    fun doOnOrgItemSelected(listener: ((Data) -> Unit)) {
        this.onClickListener = listener
    }

    override fun getItemCount(): Int = organizations.size

    inner class ViewHolder(private var item: OrgListItemBinding) :
        RecyclerView.ViewHolder(item.root) {
        fun bind(org:Data) {
            item.orgName.text =
                if (org.name.isEmpty()) "No name"
                else org.name

            item.orgDescription.text = org.no_of_members.toString() + " Members"
            Glide.with(context).load(org.logo_url).into(item.orgImg)
            item.joinSignInButton.setOnClickListener {
                onClickListener?.invoke(org)
            }
        }
    }
}
