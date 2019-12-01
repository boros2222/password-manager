package com.example.android.passwordmanager.credentialedit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.passwordmanager.database.Credential
import com.example.android.passwordmanager.database.CredentialDao
import kotlinx.coroutines.*

class CredentialEditViewModel(
        private val credentialId: Long = 0L,
        val database: CredentialDao) : ViewModel() {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var credential = MutableLiveData<Credential>()

    private val _navigateToCredentialList = MutableLiveData<Boolean>()
    val navigateToCredentialList: LiveData<Boolean>
        get() = _navigateToCredentialList

    private val _dataLoaded = MutableLiveData<Boolean>()
    val dataLoaded: LiveData<Boolean>
        get() = _dataLoaded

    init {
        _dataLoaded.value = false
        _navigateToCredentialList.value = false
        initializeCredential()
    }

    private fun initializeCredential() {
        uiScope.launch {
            credential.value = getCredentialFromDatabase()
            _dataLoaded.value = true
        }
    }

    private suspend fun getCredentialFromDatabase(): Credential {
        return withContext(Dispatchers.IO) {
            when (credentialId) {
                0L -> Credential()
                else -> {
                    var credential = database.get(credentialId)

                    if (credential == null)
                        credential = Credential()

                    credential
                }
            }
        }
    }

    fun save(changedCredential: Credential) {
        uiScope.launch {
            credential.value?.website = changedCredential.website
            credential.value?.username = changedCredential.username
            credential.value?.password = changedCredential.password
            saveCredentialToDatabase()
            _navigateToCredentialList.value = true
        }
    }

    private suspend fun saveCredentialToDatabase(): Unit? {
        return withContext(Dispatchers.IO) {
            credential.value?.let { credential ->
                val id = database.insert(credential)
                if (id == -1L) {
                    database.update(credential)
                }
            }
        }
    }

    fun doneNavigating() {
        _navigateToCredentialList.value = false
    }

    fun dataHandled() {
        _dataLoaded.value = false
    }
}
