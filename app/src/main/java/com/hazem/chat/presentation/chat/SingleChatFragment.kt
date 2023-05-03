package com.hazem.chat.presentation.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.hazem.chat.R
import com.hazem.chat.presentation.chat.viewmodel.SingleChatViewModel
import com.hazem.chat.utils.Constants.Companion.USER_ID_KEY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SingleChatFragment : Fragment() {
private val singleChatViewModel:SingleChatViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_single_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = singleChatViewModel.readFromSP(USER_ID_KEY, String::class.java)

    }
}