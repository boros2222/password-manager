package com.example.android.passwordmanager.credentiallist

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
import com.example.android.passwordmanager.database.CredentialDatabase
import com.example.android.passwordmanager.databinding.FragmentCredentialListBinding
import com.google.android.material.snackbar.Snackbar

class CredentialListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentCredentialListBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_credential_list, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = CredentialDatabase.getInstance(application).credentialDao

        val viewModelFactory = CredentialListViewModelFactory(dataSource, application)

        val credentialListViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(CredentialListViewModel::class.java)

        binding.credentialListViewModel = credentialListViewModel

        val adapter = CredentialAdapter(
                {id: Long -> credentialListViewModel.delete(id)},
                {id: Long -> onEditCredential(id)})

        binding.credentialList.adapter = adapter

        credentialListViewModel.credentials.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })

        credentialListViewModel.showSnackbar.observe(this, Observer {
            if (it) {
                Snackbar.make(
                        activity!!.findViewById(android.R.id.content),
                        getString(R.string.cleared_message),
                        Snackbar.LENGTH_SHORT
                ).show()
                credentialListViewModel.doneShowingSnackbar()
            }
        })

        binding.newCredentialButton.setOnClickListener {
            onEditCredential()
        }

        binding.apiRequestButton.setOnClickListener {
            onApiRequest()
        }

        return binding.root
    }

    private fun onEditCredential(id: Long = 0L) {
        this.findNavController().navigate(CredentialListFragmentDirections
                .actionCredentialListFragmentToCredentialEditFragment(id))
    }

    private fun onApiRequest() {
        this.findNavController().navigate(CredentialListFragmentDirections
                .actionCredentialListFragmentToPostListFragment())
    }

}
