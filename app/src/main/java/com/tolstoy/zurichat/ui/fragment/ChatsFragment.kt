package com.tolstoy.zurichat.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.models.DmMessages
import com.tolstoy.zurichat.ui.activities.DMActivity
import com.tolstoy.zurichat.ui.activities.MainActivity
import com.tolstoy.zurichat.ui.adapters.RecyclerViewAdapter
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatsFragment : Fragment() {

    private val ARG_COLUMN_COUNT: String? = "column-count"
    private var mColumnCount = 1

    var messages: ArrayList<DmMessages>? = null
    private var displayedList: List<DmMessages?>? = null

    private var mRootView: View? = null
    var list: RecyclerView? = null
    var msgAdapter: RecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

                mColumnCount =
                    requireArguments().getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_chats, container, false)

        messages= ArrayList<DmMessages>()

        messages!!.add(DmMessages("Mary Basset", "Hey what's good", "22"))
        messages!!.add(DmMessages("Druids", "Hey what's good", "1"))
        messages!!.add(DmMessages("Kolade", "Hey what's good", "5"))
        messages!!.add(DmMessages("Hamid.O", "Hey what's good", "3"))
        messages!!.add(DmMessages("Luxanne", "Hey what's good", "7"))
        messages!!.add(DmMessages("Cephas", "Hey what's good", "10"))
        messages!!.add(DmMessages("Mark", "Hey what's good", "3"))
        messages!!.add(DmMessages("John Victor", "Hey what's good", "2"))
        messages!!.add(DmMessages("Hillary Jackson", "Hey what's good", "8"))

        list = mRootView?.findViewById(R.id.recycler)

        // Set the adapter
        println(messages)
        // Set the adapter

            val context = (list as RecyclerView).context
            val recyclerView = list as RecyclerView
            recyclerView.layoutManager = LinearLayoutManager(context)
            msgAdapter = RecyclerViewAdapter(requireActivity(),messages!!)
            list?.adapter = msgAdapter

        //let's implement a click listener for our listView
        //this will execute when the user selects a note item from the listView
        list?.setOnClickListener() {
            startActivity(Intent(activity, DMActivity::class.java))
        }

        mRootView?.findViewById<FloatingActionButton>(R.id.fab_add_chat)?.setOnClickListener {
            startActivity(Intent(activity, DMActivity::class.java))
        }

        return mRootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param columnCount the column position.
         * @return A new instance of fragment ChatsFragment.
         */
        @JvmStatic
        fun newInstance(columnCount: Int) =
            ChatsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }


}