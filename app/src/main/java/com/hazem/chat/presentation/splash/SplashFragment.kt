package com.hazem.chat.presentation.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hazem.chat.R
import com.hazem.chat.databinding.FragmentSplashBinding
import com.hazem.chat.presentation.splash.viewmodel.SplashViewModel
import com.hazem.chat.utils.Constants.Companion.HANDLER_DELAY
import com.hazem.chat.utils.Constants.Companion.IS_LOGGED_IN_KEY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private val splashViewModel: SplashViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            handleNextNavigation()
        }, HANDLER_DELAY)
    }
    private fun handleNextNavigation() {
        if (splashViewModel.getFromSP(IS_LOGGED_IN_KEY, Boolean::class.java)) {
            findNavController().navigate(R.id.action_splashFragment_to_singleChatFragment)
        } else { // not first time
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)

        }
    }
}