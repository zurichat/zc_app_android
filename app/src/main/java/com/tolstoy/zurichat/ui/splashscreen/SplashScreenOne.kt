package com.tolstoy.zurichat.ui.splashscreen

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentSplashScreenOneBinding
import kotlinx.coroutines.delay


class SplashScreenOne : Fragment(R.layout.fragment_splash_screen_one) {

    private var _binding:  FragmentSplashScreenOneBinding? = null
    private val binding get () = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSplashScreenOneBinding.bind(view)

        lifecycleScope.launchWhenStarted {
            delay(3000)
            FragmentNavigatorExtras(binding.splashImageView to "splash_logo_first")
            findNavController().navigate(R.id.action_splashScreenOneFragment_to_splashScreenTwoFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}