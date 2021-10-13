package com.zurichat.app.ui.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zurichat.app.R
import com.zurichat.app.databinding.OrgListItemBinding
import com.zurichat.app.models.User
import com.zurichat.app.models.organization_model.OrgData
import com.zurichat.app.ui.organizations.utils.ZuriSharePreference

class SwitchUserOrganizationAdapter(private var organizations: List<OrgData>,
                                    val context: Context,val user:User,var callback: OnBackPressedCallback?)
    : RecyclerView.Adapter<SwitchUserOrganizationAdapter.ViewHolder>(),Filterable {

    private val backupList = organizations
    private var onClickListener: ((orgData:OrgData, user:User) -> Unit)? = null

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

    fun doOnOrgItemSelected(listener: ((OrgData,User) -> Unit)) {
        this.onClickListener = listener
    }

    override fun getItemCount(): Int = organizations.size

    inner class ViewHolder(private var item: OrgListItemBinding) : RecyclerView.ViewHolder(item.root) {
        fun bind(org:OrgData) {
            item.orgName.text =
                if (org.name.isEmpty()) "No name"
                else org.name

            item.orgDescription.text = org.no_of_members.toString() + " Members"
            Glide.with(context).load(org.logo_url).into(item.orgImg)
            item.joinSignInButton.setOnClickListener {
                if (callback!=null){
                    callback?.remove()
                }
                ZuriSharePreference(context).setString("Current Organization ID",org.id)
                //add organization and user to a bundle and attach the bundle to the NavController
                val bundle = Bundle()
                bundle.putParcelable("Organization",org)
                bundle.putParcelable("USER",user)
                bundle.putString("org_name",org.name)
                bundle.putString("mem_id", org.member_id)
                Navigation.findNavController(item.root).navigate(R.id.homeScreenFragment, bundle)
            }
        }
    }

    override fun getFilter(): Filter {
        return _filter
    }

    val _filter = object: Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = mutableListOf<OrgData>()

            for(item in backupList) {
                if (item.name.contains(constraint!!, true)) {
                    filteredList.add(item)
                }
            }
            return FilterResults().apply { values = filteredList }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            val result = results!!.values as List<OrgData>

            if (result.isNotEmpty()) {
                organizations = result
                notifyDataSetChanged()

            } else {
                organizations = backupList
                notifyDataSetChanged()
            }
        }

    }
}
