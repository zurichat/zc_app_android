package com.zurichat.app.ui.screens

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.zurichat.app.data.remoteSource.postValue
import com.zurichat.app.data.repository.OrganizationRepository
import com.zurichat.app.databinding.ContactListItemBinding
import com.zurichat.app.databinding.ListItemSelectMember2Binding
import com.zurichat.app.models.network_response.OrganizationMembers.OrganizationMember
import com.zurichat.app.models.network_response.OrganizationMembers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created on 13-Oct-21 at 5:27 AM
 */
@HiltViewModel
class NewChannelViewModel @Inject constructor(
    private val orgRepo: OrganizationRepository
): ViewModel() {

    init {
        getMembers()
    }

    // Variables that save the state of the UI
    var currentPage = 0
    var isChannelPublic = true
    val selectedMembers = mutableListOf<OrganizationMember>()

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _organizationMembersResponse = MutableLiveData<OrganizationMembers>()
    val organizationMembersResponse: LiveData<OrganizationMembers> get() = _organizationMembersResponse

    fun getMembers() = viewModelScope.launch {
        _organizationMembersResponse.postValue(orgRepo.getMembers(), _error)
    }
}

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created on 13-Oct-21 at 6:22 AM
 */
class MembersAdapter(val members: List<OrganizationMember>):
    RecyclerView.Adapter<MembersAdapter.ViewHolder>(){

    var listener: ((OrganizationMember) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ContactListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position)

    override fun getItemCount() = members.size

    inner class ViewHolder(val binding: ContactListItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) = with(binding){
            contactName.text = members[position].name()
            contactStatus.text = members[position].email
            root.setOnClickListener {
                listener?.invoke(members[position])
            }
        }
    }
}

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created on 13-Oct-21 at 6:25 AM
 */
class SelectedMembersAdapter(private val selectedMembers: MutableList<OrganizationMember>):
    RecyclerView.Adapter<SelectedMembersAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ListItemSelectMember2Binding
            .inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position)

    override fun getItemCount() = selectedMembers.size

    fun add(member: OrganizationMember){
        selectedMembers.add(member)
        notifyItemInserted(selectedMembers.lastIndex)
    }

    inner class ViewHolder(val binding: ListItemSelectMember2Binding): RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) = with(binding){
            nameOfContact.text = selectedMembers[position].name()
            root.setOnClickListener {
                selectedMembers.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }
}