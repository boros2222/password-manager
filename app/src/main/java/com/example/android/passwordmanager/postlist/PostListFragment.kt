package com.example.android.passwordmanager.postlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.android.passwordmanager.R
import com.example.android.passwordmanager.databinding.FragmentPostListBinding
import com.google.android.material.snackbar.Snackbar

class PostListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentPostListBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_post_list, container, false)

        val application = requireNotNull(this.activity).application

        val viewModelFactory = PostListViewModelFactory(application)

        val postListViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(PostListViewModel::class.java)

        binding.postListViewModel = postListViewModel

        val adapter = PostAdapter()

        binding.postList.adapter = adapter

        postListViewModel.posts.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })

        postListViewModel.numberOfPosts.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.numberOfPostsText.text = getString(R.string.number_of_posts, it)
            }
        })

        postListViewModel.showSnackbar.observe(this, Observer {
            if (it) {
                Snackbar.make(
                        activity!!.findViewById(android.R.id.content),
                        getString(R.string.api_request_failed),
                        Snackbar.LENGTH_SHORT
                ).show()
                postListViewModel.doneShowingSnackbar()
            }
        })

        binding.backButton.setOnClickListener {
            onCancel()
        }

        return binding.root
    }

    private fun onCancel() {
        this.findNavController().navigate(PostListFragmentDirections
                .actionPostListFragmentToCredentialListFragment())
    }

}
