package com.tolstoy.zurichat.ui.splashscreen

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.View
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentSplashScreenTwoBinding


class SplashScreenTwo : Fragment(R.layout.fragment_splash_screen_two) {

    private var _binding: FragmentSplashScreenTwoBinding? = null
//    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val animation = TransitionInflater.from(requireContext()).inflateTransition(
            android.R.transition.move
        )
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSplashScreenTwoBinding.bind(view)
    }
}