package com.example.android.passwordmanager.credentialedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.passwordmanager.database.CredentialDao

class CredentialEditViewModelFactory(
        private val credentialId: Long,
        private val dataSource: CredentialDao) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CredentialEditViewModel::class.java)) {
            return CredentialEditViewModel(credentialId, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
