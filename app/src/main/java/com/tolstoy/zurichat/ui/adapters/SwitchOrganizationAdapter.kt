package com.tolstoy.zurichat.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.OrgListItemBinding
import com.tolstoy.zurichat.models.organization_model.UserOrganizationData


class SwitchOrganizationAdapter(val fragment: Fragment): RecyclerView.Adapter<SwitchOrganizationAdapter.ViewHolder>() {
    var list = emptyList<UserOrganizationData>()
    val _list: List<UserOrganizationData> by lazy { list }


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
        holder.bind(list[position])
    }

    override fun getItemCount(): Int  = list.size

//     Return response for user organization:
//    "id": "6151e85375689e8df18f7bbf",
//    "imgs": [
//    ""
//    ],
//    "isOwner": true,
//    "logo_url": "",
//    "name": "Zuri Chat",
//    "no_of_members": 1,
//    "workspace_url": "zurichat-uzd5370.zurichat.com"

   inner class ViewHolder(private var item: OrgListItemBinding): RecyclerView.ViewHolder(item.root){
       @SuppressLint("SetTextI18n")
       fun bind(org: UserOrganizationData) {
           item.orgName.text =
               if(org.name.isEmpty()) "No name"
               else org.name

           item.orgDescription.text = org.no_of_members.toString() + " Members"
           item.orgImg.setBackgroundResource(R.drawable.choose_wallpaper_4)

           item.joinSignInButton.setOnClickListener {
               // will crash the app because no value is being passed
               it.findNavController().navigate(R.id.dmFragment)
           }
       }
    }
}