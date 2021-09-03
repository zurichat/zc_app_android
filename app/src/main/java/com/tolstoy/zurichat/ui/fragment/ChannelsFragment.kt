package com.tolstoy.zurichat.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.util.viewBinding
import com.tolstoy.zurichat.databinding.FragmentChannelsBinding
import com.tolstoy.zurichat.ui.adapters.ReadChannelMessageAdapter
import com.tolstoy.zurichat.ui.adapters.UnreadChannelMessageAdapter


class ChannelsFragment : Fragment(R.layout.fragment_channels) {
    // TODO: Rename and change types of parameters

   private val binding by viewBinding(FragmentChannelsBinding::bind)
    //Dummy List for populating the recyclerView

    private var channelList = mutableListOf<Channel>(
        Channel("stage_4", true, false),
        Channel("announcement", false, false),
        Channel("comedy", false, true),
        Channel("team_tolstoy", true, true),
        Channel("resources", false, false),
        Channel("stage_5", true, false),
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


     binding.unreadRecycler.layoutManager =
         LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        val adapt = UnreadChannelMessageAdapter(requireActivity(),channelList)
        binding.unreadRecycler.adapter = adapt

        binding.readRecycler.layoutManager =
            LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        val adapt2 = ReadChannelMessageAdapter(requireActivity(), channelList)
        binding.readRecycler.adapter = adapt2

    }

}
//Dummy Data for populating the recyclerView
data class Channel(var name: String, var privacy: Boolean, var read: Boolean )

/*// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

*/

/* private var param1: String? = null
private var param2: String? = null

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
        param1 = it.getString(ARG_PARAM1)
        param2 = it.getString(ARG_PARAM2)
    }
}

override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_channels, container, false)
}
*/

/*   companion object {
       *//**
 * Use this factory method to create a new instance of
 * this fragment using the provided parameters.
 *
 * @param param1 Parameter 1.
 * @param param2 Parameter 2.
 * @return A new instance of fragment CallsFragment.
 *//*
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChannelsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }*/