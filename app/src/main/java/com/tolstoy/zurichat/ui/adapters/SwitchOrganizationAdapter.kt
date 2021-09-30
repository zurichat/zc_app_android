package com.tolstoy.zurichat.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.OrgListItemBinding
import com.tolstoy.zurichat.models.organization_model.Data


class SwitchOrganizationAdapter(val list: List<Data>, val context: Context): RecyclerView.Adapter<SwitchOrganizationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwitchOrganizationAdapter.ViewHolder {
        return ViewHolder(
            OrgListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], context = context)
    }

    override fun getItemCount(): Int  = list.size

   inner class ViewHolder(private var item: OrgListItemBinding): RecyclerView.ViewHolder(item.root){
       @SuppressLint("SetTextI18n")
       fun bind(org: Data, context: Context) {
           item.orgName.text =
               if(org.name.isEmpty()) "No name"
               else org.name

           item.orgDescription.text = org.no_of_members.toString() + " Members"
           Glide.with(context).load(org.logo_url).into(item.orgImg)

           item.joinSignInButton.setOnClickListener {
               // will crash the app because no value is being passed
               it.findNavController().navigate(R.id.dmFragment)
           }
       }
    }
}