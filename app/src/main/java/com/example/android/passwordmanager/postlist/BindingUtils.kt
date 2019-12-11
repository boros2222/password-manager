package com.example.android.passwordmanager.postlist

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("title")
fun TextView.setTitle(item: Post) {
    text = item.title
}

@BindingAdapter("body")
fun TextView.setBody(item: Post) {
    text = item.body
}