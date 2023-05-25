package com.hazem.chat.presentation.chat.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hazem.chat.databinding.ChatItemBinding
import com.hazem.chat.domain.model.User
import com.hazem.chat.presentation.login.adapter.OnItemClick

class ContactsAdapter : RecyclerView.Adapter<ContactsAdapter.MyViewHolder>() {
    private var contacts: List<User> = arrayListOf()
    var onItemClicked: OnItemClick<User>? =null
    inner class MyViewHolder(private val binding: ChatItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User, position: Int) {
            binding.tvUserName.text = user.name
            binding.layout.setOnClickListener {
                onItemClicked?.onItemClicked(user,position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ChatItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       holder.bind(contacts[position], position)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(contacts: ArrayList<User>) {
        this.contacts = contacts
        notifyDataSetChanged()
    }
}