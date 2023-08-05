package com.hazem.chat.presentation.profile

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.hazem.chat.databinding.FragmentProfileBinding
import com.hazem.chat.domain.model.User
import com.hazem.chat.presentation.profile.viewmodel.GetUserByIdState
import com.hazem.chat.presentation.profile.viewmodel.ProfileViewModel
import com.hazem.chat.utils.Constants
import com.hazem.chat.utils.createAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var dialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = requireContext().createAlertDialog(requireActivity())
        //binding.btnUpdate
        observe()
        profileViewModel.fetchCurrentUserById(profileViewModel.getFromSP(Constants.USER_ID_KEY,String::class.java))
        getUserObserve()
    }

    private fun getUserObserve() {
        lifecycleScope.launch {
            profileViewModel.getUserState.collect {
                when (it) {
                    is GetUserByIdState.GetUserByIdSuccessfully -> setUSerDate(it.message)
                    GetUserByIdState.Init -> Unit
                    is GetUserByIdState.ShowError -> {
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

    private fun setUSerDate(data: User) {
        binding.etUsername.setText( data.name)
        binding.etPhoneNumber.setText( data.phoneNumber)
        binding.ivUserPhoto.load(data.uri)
    }

    private fun observe() {
        lifecycleScope.launch {
            profileViewModel.updateProfileState.collect {
                when (it) {
                    UpdateProfileState.Init -> Unit
                    is UpdateProfileState.ShowError -> {
                        Snackbar.make(
                            requireActivity().findViewById(android.R.id.content),
                            it.message,
                            2000
                        ).show()
                    }

                    is UpdateProfileState.UpdatedSuccessfully -> {

                        Toast.makeText(
                            requireContext(),
                            it.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    is UpdateProfileState.IsLoading -> handleLoadingState(it.isLoading)
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