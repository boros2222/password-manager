package com.example.android.passwordmanager.postlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class PostListViewModel(
        application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>>
        get() = _posts

    val numberOfPosts: LiveData<Int> = Transformations.map(posts) { posts ->
        posts.size
    }

    private val _showSnackbar = MutableLiveData<Boolean>()
    val showSnackbar: LiveData<Boolean>
        get() = _showSnackbar

    init {
        _showSnackbar.value = false
        getPostsFromApi()
    }

    private fun getPostsFromApi() {
        coroutineScope.launch {
            val getPropertiesDeferred = PostApi.retrofitService.getProperties()
            try {
                _posts.value = getPropertiesDeferred.await()
            } catch (e: Exception) {
                _showSnackbar.value = true
            }
        }
    }

    fun doneShowingSnackbar() {
        _showSnackbar.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
