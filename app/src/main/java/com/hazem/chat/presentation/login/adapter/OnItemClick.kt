package com.hazem.chat.presentation.login.adapter

interface OnItemClick<T> {
    fun onItemClicked(item:T,position:Int)
}