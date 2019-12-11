package com.example.android.passwordmanager.credentiallist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.passwordmanager.database.CredentialDao
import kotlinx.coroutines.*

class CredentialListViewModel(
        val database: CredentialDao,
        application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val credentials = database.getAllCredentials()

    private val _navigateToCredentialEdit = MutableLiveData<Long>()
    val navigateToCredentialEdit: LiveData<Long>
        get() = _navigateToCredentialEdit

    private val _showSnackbar = MutableLiveData<Boolean>()
    val showSnackbar: LiveData<Boolean>
        get() = _showSnackbar

    init {
        _showSnackbar.value = false
    }

    fun delete(credentialId: Long) {
        uiScope.launch {
            clear(credentialId)
            _showSnackbar.value = true
        }
    }

    private suspend fun clear(credentialId: Long) {
        withContext(Dispatchers.IO) {
            database.clear(credentialId)
        }
    }

    fun edit(id: Long) {
        _navigateToCredentialEdit.value = id
    }

    fun doneNavigatingToCredentialEdit() {
        _navigateToCredentialEdit.value = null
    }

    fun doneShowingSnackbar() {
        _showSnackbar.value = false
    }
}
