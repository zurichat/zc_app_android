package com.zurichat.app.ui.fragments.switch_account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zurichat.app.R
import com.zurichat.app.models.User


class AccountsFragment : Fragment() {
    private lateinit var user : User
    private lateinit var accountViewModel: UserViewModel
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
        //setup toolbar with navgraph
        val navController = findNavController()
        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar3)
        //toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setupWithNavController(navController)

        //pass current user to the confirm password screen using navgraph args
        val curUser = args.currentUser
        val adapter = UserAccountAdapter(curUser)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_accts)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())

        //Show textview text when no account available for switching
        val textView = view.findViewById<TextView>(R.id.no_acct_txv)
        accountViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        accountViewModel.readAllData.observe(viewLifecycleOwner, Observer {user->
            if (!user.isNullOrEmpty())textView.visibility = View.GONE
            adapter.setData(user)
            if (user.isNotEmpty()){
                toolbar.subtitle = user.size.toString()+ " Account(s)"
            }
        })

    }


}