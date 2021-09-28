package com.tolstoy.zurichat.ui.adapters

import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentCreateOrganizationsBinding
import com.tolstoy.zurichat.databinding.NewChannelItemBinding
import com.tolstoy.zurichat.databinding.OrgListItemBinding
import com.tolstoy.zurichat.models.OrganizationModel.Organization
import com.tolstoy.zurichat.models.OrganizationModel.OrganizationData
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.ui.organizations.CreateOrganizationsFragment
import com.tolstoy.zurichat.util.viewBinding


class CreateOrganizationAdapter(val fragment: Fragment): RecyclerView.Adapter<CreateOrganizationAdapter.ViewHolder>() {
    var list = emptyList<OrganizationData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateOrganizationAdapter.ViewHolder {
        return ViewHolder(
            OrgListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }


//    OrganizationData(
//    val _id: String,
//    val admins: Any,
//    val created_at: String,
//    val creator_email: String,
//    val creator_id: String,
//    val logo_url: String,
//    val name: String,
//    val plugins: Any,
//    val settings: Any,
//    val updated_at: String,
//    val workspace_url: String

//    Organization(
//    val organizationData: OrganizationData,
//    val message: String,
//    val status: Int
//    )

   inner class ViewHolder(private var item: OrgListItemBinding): RecyclerView.ViewHolder(item.root){
       fun bind(org: OrganizationData) {
           item.orgName.text =
               if(org.name.isEmpty()) "No name"
               else org.name


           item.orgDescription.text = org.workspace_url
           item.orgImg.setBackgroundResource(R.drawable.ic_kolade_icon)

           item.joinSignInButton.setOnClickListener {
               // will crash the app because no value is being passed
               it.findNavController().navigate(R.id.dmFragment)
           }

       }
    }
}