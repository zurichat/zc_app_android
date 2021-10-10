package com.zurichat.app.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zurichat.app.R
import com.zurichat.app.databinding.OrgListItemBinding
import com.zurichat.app.models.organization_model.Data

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
                //add organization name and id to a bundle and attach the bundle to the NavController
                val bundle = bundleOf(
                    "org_name" to org.name,
                    "org_id" to org.id
                )
                Navigation.findNavController(item.root).navigate(R.id.homeScreenFragment, bundle)
            }
        }
    }
}
