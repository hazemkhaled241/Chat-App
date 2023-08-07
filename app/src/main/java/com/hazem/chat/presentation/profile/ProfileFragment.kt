package com.hazem.chat.presentation.profile

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.hazem.chat.databinding.FragmentProfileBinding
import com.hazem.chat.domain.model.User
import com.hazem.chat.presentation.profile.viewmodel.GetUserByIdState
import com.hazem.chat.presentation.profile.viewmodel.ProfileViewModel
import com.hazem.chat.presentation.profile.viewmodel.UpdateProfileState
import com.hazem.chat.utils.Constants
import com.hazem.chat.utils.FileUtil
import com.hazem.chat.utils.createAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var dialog: Dialog
    private var user: User? = null
    private var imageUri: Uri? = null

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
        profileViewModel.fetchCurrentUserById(
            profileViewModel.getFromSP(
                Constants.USER_ID_KEY,
                String::class.java
            )
        )
        getUserObserve()
        binding.btnUpdate.setOnClickListener {
            handleUpdateProfile()
        }

        val galleryLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    var compressedFile: File?
                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {

                        val data = result.data
                        val imgUri = data?.data
                        if (imgUri != null) {
                            compressedFile = FileUtil.from(requireContext(), imgUri)?.let { it1 ->
                                Compressor.compress(
                                    requireContext(),
                                    it1
                                )
                            }
                            imageUri = Uri.fromFile(compressedFile)
                            binding.ivUserPhoto.load(imageUri)
                        }
                    }
                }
            }

        binding.ivUserPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryLauncher.launch(intent)
        }
    }

    private fun handleUpdateProfile() {
        if (binding.etUsername.text.toString() == user?.name && imageUri == user?.uri)
            Toast.makeText(requireContext(), "There is no data changed", Toast.LENGTH_LONG).show()
        else if (imageUri.toString() != user?.uri.toString() && binding.etUsername.text.toString() != user?.name) {
            user?.let {
                profileViewModel.updateProfile(
                    it.userId,
                    User(it.userId, it.phoneNumber, binding.etUsername.text.toString(), imageUri!!)
                )
            }
        } else if (imageUri.toString() != user?.uri.toString()) {
            user?.let {
                profileViewModel.updateProfile(
                    it.userId,
                    User(it.userId, it.phoneNumber, it.name, imageUri!!)
                )
            }
        } else
            user?.let {
                profileViewModel.updateProfile(
                    it.userId,
                    User(it.userId, it.phoneNumber, binding.etUsername.text.toString(), it.uri)
                )
            }

    }

    private fun getUserObserve() {
        lifecycleScope.launch {
            profileViewModel.getUserState.collect {
                when (it) {
                    is GetUserByIdState.GetUserByIdSuccessfully -> {
                        user = it.message
                        imageUri = it.message.uri
                        setUserDate(it.message)
                    }

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

    private fun setUserDate(data: User) {
        binding.etUsername.setText(data.name)
        binding.etPhoneNumber.setText(data.phoneNumber)
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