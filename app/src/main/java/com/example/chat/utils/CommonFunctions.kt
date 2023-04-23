package com.example.chat.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.*

import com.example.chat.R



@SuppressLint("InflateParams")
fun Context.createAlertDialog(activity:Activity): Dialog {
   val dialogTransparent = Dialog(this, android.R.style.Theme_Black)
    val view: View = LayoutInflater.from(activity).inflate(
        R.layout.progress_dialog, null
    )
    dialogTransparent.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialogTransparent.window?.setBackgroundDrawableResource(
        R.color.transparent
    )

    dialogTransparent.setCancelable(false)
    dialogTransparent.setContentView(view)
    return dialogTransparent
}
