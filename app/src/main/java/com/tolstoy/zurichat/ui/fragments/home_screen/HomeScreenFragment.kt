package com.tolstoy.zurichat.ui.fragments.home_screen

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentHomeScreenBinding
import com.tolstoy.zurichat.ui.adapters.HomeFragmentPagerAdapter
import com.tolstoy.zurichat.ui.newchannel.NewChannelActivity
import com.tolstoy.zurichat.ui.settings.SettingsActivity
import com.tolstoy.zurichat.util.setUpApplicationTheme

class HomeScreenFragment : Fragment() {

    private lateinit var binding: FragmentHomeScreenBinding

    private val tabTitles = intArrayOf(R.string.chats, R.string.channels)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPagerAdapter = HomeFragmentPagerAdapter(requireActivity())
        val viewPager = binding.pager
        val tabs = binding.tabs
        val toolbar = binding.toolbarContainer.toolbar

        // setup for viewpager2 and tab layout
        viewPager.adapter = viewPagerAdapter

        // attaches the viewpager to the tabs layout
        TabLayoutMediator(
            tabs, viewPager
        ) { tab, position ->
            tab.text = resources.getString(
                tabTitles[position]
            )
        }.attach()


        toolbar.setOnMenuItemClickListener {

            when(it.itemId){
                R.id.settings -> {
                    findNavController().navigate(R.id.settingsActivity)
                }
                R.id.search -> {}
                R.id.new_channel -> {}
                R.id.saved_messages -> {}
            }
            true
        }



    }

    /**private fun processSearch(item: MenuItem?) {
        val s = SpannableString("My MenuItem")
        s.setSpan(ForegroundColorSpan(Color.WHITE), 0, s.length, 0)
        if (item != null) {
            item.title = s
        }
        searchView?.setOnSearchClickListener {
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    var str = rcAdapter?.filter(query.toString())

                    if (str == null) {
                        Toast.makeText(
                            this@MainActivity,
                            "No Match found",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    rcAdapter?.filter(newText.toString())
                    return true
                }
            }
        }
    }
*/

}