package com.hazem.chat.presentation.chat

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseUser
import com.hazem.chat.databinding.FragmentSingleChatBinding
import com.hazem.chat.domain.model.Message
import com.hazem.chat.presentation.chat.adapter.MessageAdapter
import com.hazem.chat.presentation.chat.viewmodel.SingleChatState
import com.hazem.chat.presentation.chat.viewmodel.SingleChatViewModel
import com.hazem.chat.utils.FileUtil
import com.hazem.chat.utils.createAlertDialog
import com.hazem.chat.utils.showErrorSnackBar
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*


@AndroidEntryPoint
class SingleChatFragment : Fragment() {
    private var _binding: FragmentSingleChatBinding? = null
    private val binding get() = _binding!!
    private val singleChatViewModel: SingleChatViewModel by viewModels()
    private var firebaseCurrentUser: FirebaseUser? = null
    private var messages: ArrayList<Message> = arrayListOf()
    private val messageAdapter: MessageAdapter by lazy { MessageAdapter() }
    private lateinit var dialog: Dialog
    private val args: SingleChatFragmentArgs by navArgs()
    private lateinit var listener: ViewTreeObserver.OnGlobalLayoutListener
    private  var receiverUserId: String? = null
    private lateinit var imageUris: Uri


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _binding = FragmentSingleChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseCurrentUser = singleChatViewModel.getFirebaseCurrentUser()
        dialog = requireContext().createAlertDialog(requireActivity())

        receiverUserId = args.user.userId
        binding.tvUserName.text =args.user.name
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        fetchMessagesBetweenTwoUsers()
        binding.btnSendMessage.setOnClickListener {
            val message = prepareMessage()
            singleChatViewModel.requestSendMessage(message)
            singleChatViewModel.scroll=false
        }

       /* binding.ivUploadImage.setOnClickListener {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }*/


        observe()
        handleKeyboard()

        binding.rvMessages.adapter = messageAdapter
    }

    private fun fetchMessagesBetweenTwoUsers() {
        firebaseCurrentUser?.uid?.let { currentId ->
            singleChatViewModel.requestFetchMessagesBetweenTwoUsers(
                senderId = currentId,
                receiverId = receiverUserId!!
            )
        }
    }
    private fun prepareMessage(): Message {

        return Message(
            messageId = "",
            senderId = firebaseCurrentUser!!.uid,
            receiverId = receiverUserId!!,
            messageText = binding.etInputText.text.toString().trim(),
            imageUrl = null,
            date = Date()
        )
    }
    private fun showMessages(messages: ArrayList<Message>) {
        firebaseCurrentUser?.uid?.let { currentId ->
            messageAdapter.addMessages(
                newMessages = messages,
                currentUserId = currentId
            )
            binding.rvMessages.scrollToPosition(messages.size - 1)
        }
    }


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // PERMISSION GRANTED
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startForProfileImageResult.launch(galleryIntent)
        } else {
            // PERMISSION NOT GRANTED
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        }
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            val resultCode: Int = activityResult.resultCode
            val data: Intent? = activityResult.data
            when (resultCode) {
                Activity.RESULT_OK -> {
                    var compressedFile: File?
                    data?.let {
                        it.data?.let { uri ->
                            // imageUri = uri
                            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                                compressedFile = FileUtil.from(requireContext(), uri)?.let { it1 ->
                                    Compressor.compress(
                                        requireContext(),
                                        it1
                                    )
                                }
                                //  uploadSellAdvertisementViewModel.addImagesUris(imageUris)
                                withContext(Dispatchers.Main) {
                                    imageUris = Uri.fromFile(compressedFile)
                                    // uploadedImagesAdapter.updateList(imageUris)
                                    singleChatViewModel.requestSendMessage(
                                        Message(
                                            messageId = "",
                                            senderId = firebaseCurrentUser!!.uid,
                                            receiverId = receiverUserId!!,
                                            messageText = null,
                                            imageUrl = imageUris,
                                            date = Date()
                                        )
                                    )
                                    dismissLoadingDialog()
                                }
                            }
                        }
                    }
                }
                else -> {
                    dismissLoadingDialog()
                }
            }
        }
    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            singleChatViewModel.chatRoomState.collect {
                when (it) {
                    SingleChatState.Init -> Unit
                    is SingleChatState.IsLoading -> handleLoadingState(it.isLoading)
                    is SingleChatState.FetchMessagesSuccessfullySuccessfully -> {
                        messages = it.messages.toCollection(ArrayList())
                        if (messages.isNotEmpty()) {
                            if (messages.last().senderId == firebaseCurrentUser!!.uid)
                                binding.etInputText.setText("")
                        }
                        showMessages(messages)
                    }
                    is SingleChatState.SendMessageSuccessfully -> {
                        binding.etInputText.text?.clear()
                        // messages.add(it.message)
                        showMessages(messages)
                    }
                    is SingleChatState.ShowError -> handleErrorState(it.message)
                }
            }
        }
    }
    private fun handleKeyboard() {
        listener = ViewTreeObserver.OnGlobalLayoutListener {
            val r = Rect()
            binding.clChatMessages.getWindowVisibleDisplayFrame(r)

            val screenHeight = binding.clChatMessages.rootView.height
            val keypadHeight = screenHeight - r.bottom
            if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                // Keyboard is shown
                binding.tilInput.setPadding(0,0,0,21)
            }
            else{
                binding.tilInput.setPadding(0,0,0,0)
            }
        }

        binding.clChatMessages.viewTreeObserver.addOnGlobalLayoutListener(listener)
    }
    private fun handleLoadingState(isLoading: Boolean) {
        when (isLoading) {
            true -> {
                startLoadingDialog()
            }
            false -> dismissLoadingDialog()
        }
    }

    private fun handleErrorState(message: String) {
        binding.root.showErrorSnackBar(message)
    }

    private fun startLoadingDialog() {
        dialog.create()
        dialog.show()
    }

    private fun dismissLoadingDialog() {
        dialog.dismiss()
    }


    override fun onStop() {
        singleChatViewModel.scroll=false
        binding.clChatMessages.viewTreeObserver.removeOnGlobalLayoutListener(listener) // to prevent null pointer exception and memory leaks
        super.onStop()
    }
    override fun onDestroy() {

       // binding.rvMessages.adapter = null
       // _binding = null
        super.onDestroy()
    }

    override fun onResume() {
        handleKeyboard()
        super.onResume()
    }
}