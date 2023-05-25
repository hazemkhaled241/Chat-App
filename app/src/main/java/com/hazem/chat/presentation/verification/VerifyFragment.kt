package com.hazem.chat.presentation.verification

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.hazem.chat.R
import com.hazem.chat.databinding.FragmentVerifyBinding
import com.hazem.chat.presentation.verification.viewmodel.VerificationState
import com.hazem.chat.presentation.verification.viewmodel.VerifyViewModel
import com.hazem.chat.utils.Constants.Companion.IS_LOGGED_IN_KEY
import com.hazem.chat.utils.createAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VerifyFragment : Fragment() {
    private var _binding: FragmentVerifyBinding? = null
    private val binding get() = _binding!!
    private lateinit var dialog: Dialog
    private val args: VerifyFragmentArgs by navArgs()
    private val verifyViewModel: VerifyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentVerifyBinding.inflate(inflater, container, false)
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_verifyFragment_to_loginFragment)
                }
            })

        return binding.root
        //return inflater.inflate(R.layout.fragment_verify, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = requireContext().createAlertDialog(requireActivity())
        binding.otpView.setOtpCompletionListener {
            verifyViewModel.verifyOtp(it, args.verificationID, args.user)
        }
        verifyViewModel.startCountDown()
        verifyViewModel.countDownTime.observe(viewLifecycleOwner) {
            if (it != null)
                binding.tvResend.text = it
        }
        observe()
        binding.btnVerify.setOnClickListener {
            verifyViewModel.verifyOtp(binding.otpView.text.toString(), args.verificationID,args.user)
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            verifyViewModel.loginState.collect {
                when (it) {
                    VerificationState.Init -> Unit
                    is VerificationState.IsLoading -> handleLoadingState(it.isLoading)
                    is VerificationState.LoginSuccessfully -> {
                        Toast.makeText(
                            requireContext(),
                            it.message,
                            Toast.LENGTH_LONG
                        ).show()
                        findNavController().navigate(R.id.action_verifyFragment_to_chatsHomeFragment)
                        verifyViewModel.saveInSP(IS_LOGGED_IN_KEY, true)

                    }
                    is VerificationState.ShowError -> {
                        Snackbar.make(
                            requireActivity().findViewById(android.R.id.content),
                            it.message,
                            2000
                        ).show()
                    }
                }
            }
        }

    }





    private fun handleLoadingState(isLoading: Boolean) {
        when (isLoading) {
            true -> {
                dialog.show()
            }
            false -> dialog.hide()
        }
    }


}