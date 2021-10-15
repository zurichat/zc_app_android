package com.zurichat.app.ui.organizations

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.zurichat.app.R
import com.zurichat.app.databinding.FragmentSeeYourChannelBinding
import com.zurichat.app.util.viewBinding


class SeeYourChannelFragment : Fragment(R.layout.fragment_see_your_channel) {

    private val binding by viewBinding(FragmentSeeYourChannelBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
// navigate from see your channel fragment to mainActivity passing in the organization
        binding.btnSeeYourChannel.setOnClickListener {
            val bundle = bundleOf("org_name" to arguments?.getString("org_name"))
            Navigation.findNavController(it).navigate(R.id.action_seeYourChannelFragment_to_homeScreenFragment, bundle)
        }
    }

    fun setTransparentStatusBar() {
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window: Window = requireActivity().window
            val winParams: WindowManager.LayoutParams = window.attributes
            winParams.flags = winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
            window.setAttributes(winParams)
            if (nightModeFlags != Configuration.UI_MODE_NIGHT_YES) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            }
        }
    }
}