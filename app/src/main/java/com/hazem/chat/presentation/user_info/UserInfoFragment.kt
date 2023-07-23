package com.hazem.chat.presentation.user_info

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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import coil.load

import com.hazem.chat.databinding.FragmentUserInfoBinding
import com.hazem.chat.utils.FileUtil
import com.hazem.chat.utils.createAlertDialog
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


class UserInfoFragment : Fragment() {
    private var _binding: FragmentUserInfoBinding? = null
    val binding get() = _binding!!
    private var imageUri: Uri? = null
    private lateinit var dialog: Dialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = requireContext().createAlertDialog(requireActivity())
        val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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
                       imageUri= Uri.fromFile(compressedFile)
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
}