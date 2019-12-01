package com.example.android.passwordmanager.credentiallist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.passwordmanager.database.CredentialDao

class CredentialListViewModelFactory(
        private val dataSource: CredentialDao,
        private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CredentialListViewModel::class.java)) {
            return CredentialListViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
