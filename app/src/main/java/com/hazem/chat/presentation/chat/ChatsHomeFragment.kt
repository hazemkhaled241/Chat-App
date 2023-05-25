package com.hazem.chat.presentation.chat

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.hazem.chat.databinding.FragmentChatsHomeBinding
import com.hazem.chat.domain.model.Contact
import com.hazem.chat.domain.model.User
import com.hazem.chat.presentation.chat.adapter.ContactsAdapter
import com.hazem.chat.presentation.chat.viewmodel.ChatHomeState
import com.hazem.chat.presentation.chat.viewmodel.ChatHomeViewModel
import com.hazem.chat.presentation.login.adapter.OnItemClick
import com.hazem.chat.utils.createAlertDialog
import com.hazem.chat.utils.showErrorSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatsHomeFragment : Fragment(), OnItemClick<User> {
    private var _binding: FragmentChatsHomeBinding? = null
    private val binding get() = _binding!!
    private val chatHomeViewModel: ChatHomeViewModel by viewModels()
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var contactsList = arrayListOf<Contact>()
    private lateinit var dialog: Dialog
    private val contactsAdapter:ContactsAdapter by lazy{ ContactsAdapter()}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding=FragmentChatsHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = requireContext().createAlertDialog(requireActivity())
        binding.rvChats.adapter = contactsAdapter
        contactsAdapter.onItemClicked = this
        observe()
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (!it) {
                    Toast.makeText(
                        requireContext(),
                        "Need permission to fetch contact list",
                        Toast.LENGTH_LONG
                    ).show()
                    return@registerForActivityResult
                }
                fetchContacts()
            }
        permissionLauncher.launch(android.Manifest.permission.READ_CONTACTS)
    }

    @SuppressLint("Range", "Recycle")
    private fun fetchContacts() {
        val contacts = context?.contentResolver?.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        if (contacts != null) {
           // Log.d("hhhh", contacts.count.toString())
            while (contacts.moveToNext()) {
                val name =
                    contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY))
                val number =
                    contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                contactsList.add(Contact(name, number,null))

            }
           chatHomeViewModel.fetchRegisteredContacts(contactsList)
            //Log.d("hhhh", contactsList.toSet().toString())
        }
    }
    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            chatHomeViewModel.chatHomeState.collect {
                when (it) {
                    ChatHomeState.Init -> Unit
                    is ChatHomeState.IsLoading -> handleLoadingState(it.isLoading)
                    is ChatHomeState.FetchAllRegisteredContactsSuccessfullySuccessfully -> {
                        contactsAdapter.updateList(it.messages)
                    }
                    is ChatHomeState.ShowError -> handleErrorState(it.message)
                }
            }
        }
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
    override fun onItemClicked(item: User, position: Int) {
        val action = ChatsHomeFragmentDirections.actionChatsHomeFragmentToSingleChatFragment(item)
        findNavController().navigate(action)
    }

}