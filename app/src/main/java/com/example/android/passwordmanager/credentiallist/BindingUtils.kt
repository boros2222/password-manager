package com.example.android.passwordmanager.credentiallist

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.passwordmanager.database.Credential

@BindingAdapter("website")
fun TextView.setWebsite(item: Credential) {
    text = item.website
}

@BindingAdapter("username")
fun TextView.setUsername(item: Credential) {
    text = item.username
}

@BindingAdapter("password")
fun TextView.setPassword(item: Credential) {
    text = "*".repeat(item.password.length)
}