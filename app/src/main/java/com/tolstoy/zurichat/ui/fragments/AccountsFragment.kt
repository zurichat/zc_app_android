package com.tolstoy.zurichat.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.models.User


class AccountsFragment : Fragment() {
    private lateinit var user : User
    private lateinit var accountViewModel:UserViewModel
    private val args by navArgs<AccountsFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_accounts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar3)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        val textView = view.findViewById<TextView>(R.id.no_acct_txv)
        val curUser = args.currentUser
        val adapter = UserAccountAdapter(curUser)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recycler_accts)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())

        accountViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        accountViewModel.readAllData.observe(viewLifecycleOwner, Observer {user->
            if (!user.isNullOrEmpty())textView.visibility = View.GONE
            adapter.setData(user)
        })


    }

//    fun setupRecycler(){
//        val recyclerView = view?.findViewById<RecyclerView>(R.id.recycler_accts)
//        recyclerView?.adapter = UserAccountAdapter()
//        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
//    }


}