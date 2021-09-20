package com.tolstoy.zurichat.ui.newchannel.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentSelectMemberBinding
import com.tolstoy.zurichat.models.MembersData
import com.tolstoy.zurichat.ui.adapters.MemberSelectedAdapter
import com.tolstoy.zurichat.ui.adapters.SelectMemberAdapter
import com.tolstoy.zurichat.util.viewBinding
import timber.log.Timber

class SelectMemberFragment : Fragment(R.layout.fragment_select_member) {


    private val memberSelectedAdapter = MemberSelectedAdapter{
            member -> removeMember(member)
    }

    private val selectMemberAdapter = SelectMemberAdapter{
        member -> addMember(member)
    }

    private lateinit var selectedList:MutableList<MembersData>
    private lateinit var fab: FloatingActionButton
    private var selectedListOfMembers = MutableLiveData<List<MembersData>>()
    private var selectedMembers = mutableListOf<MembersData>()

    private val binding by viewBinding(FragmentSelectMemberBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab = binding.floatingActionButton2



        with(binding) {
           textView6.text = "${selectMember().size} Members"
            toolbar.setNavigationOnClickListener {
                try {
                    val action =
                        SelectMemberFragmentDirections.actionSelectMemberFragmentToSelectNewChannelFragment()
                    findNavController().navigate(action)
                } catch (exc: Exception) {
                    Timber.e(SelectNewChannelFragment.TAG, exc.toString())
                }
            }
            recyclerView.apply {
                adapter = selectMemberAdapter
                selectMemberAdapter.loadMembers(selectMember())
                layoutManager = LinearLayoutManager(requireContext())
            }
            rcvSelected.apply {
                adapter = memberSelectedAdapter

            }
            selectedListOfMembers.observe(requireActivity()){
                if (it.isEmpty()){
                    rcvSelected.visibility = View.GONE
                    fab.visibility = View.GONE
                }else{
                    rcvSelected.visibility = View.VISIBLE

                    memberSelectedAdapter.addMembers(selectedMembers)
                    textView4.text = "New Channel"
                    textView6.text = "${selectedMembers.size} out of ${selectMember().size} Selected"
                    fab.visibility = View.VISIBLE
                    rcvSelected.smoothScrollToPosition(selectedMembers.size - 1)
                }
                memberSelectedAdapter.addMembers(selectedMembers)
            }
            fab.setOnClickListener {
                try{
                    val action = SelectMemberFragmentDirections.actionSelectMemberFragmentToNewChannelDataFragment(channelName = "",memberData = selectedMembers.toTypedArray(),members = null,user = null,private = false)
                    findNavController().navigate(action)
                }catch (err: Exception){
                    Timber.e(SelectNewChannelFragment.TAG, err.printStackTrace())
                }
            }

        }

    }
    fun addMember(member: MembersData){
        binding.newChannelLayout.visibility = View.GONE
        if(!selectedMembers.contains(member)){
            selectedMembers.add(member)
            selectedListOfMembers.value = selectedMembers
        }
        else{
            removeMember(member)
        }

    }

    //Remove Contact from Selected List
    fun removeMember(member: MembersData){
        selectedMembers.remove(member)
        selectedListOfMembers.value = selectedMembers
    }

    private fun selectMember(): List<MembersData> {
                return listOf(MembersData(R.drawable.dmuser, "Peculiar Umeh", "God is great"),
        MembersData(R.drawable.dmuser, "Joseph Kalu", "I am having fun"),
        MembersData(R.drawable.dmuser, "Ahmed Johnson", "God is great"),
        MembersData(R.drawable.dmuser, "Chuks Fire", "I am happy"),
        MembersData(R.drawable.dmuser, "Lukanne Godness", "Eat, code and sleep"),
        MembersData(R.drawable.dmuser, "Sammy Bloomy", "Seeing all of you"),
        MembersData(R.drawable.dmuser, "Uche Mentessa", "I will get to level ten"),
        MembersData(R.drawable.dmuser, "Oluwaseyi Oga", "God is great"),
        MembersData(R.drawable.dmuser, "John Chumme", "God is great"))
    }

    companion object {
        const val TAG = "SelectContactFragment"

        fun newInstance(memberSelectedList: ArrayList<MembersData>): SelectMemberFragment {

            val args = Bundle()
            args.putParcelableArrayList("Selected Members", memberSelectedList)
            val frag = SelectMemberFragment()
            frag.arguments = args
            return frag
        }
    }
}