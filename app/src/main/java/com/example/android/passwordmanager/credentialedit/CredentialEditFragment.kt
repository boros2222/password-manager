package com.example.android.passwordmanager.credentialedit

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
import com.example.android.passwordmanager.database.Credential
import com.example.android.passwordmanager.database.CredentialDatabase
import com.example.android.passwordmanager.databinding.FragmentCredentialEditBinding

class CredentialEditFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentCredentialEditBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_credential_edit, container, false)

        val application = requireNotNull(this.activity).application
        val arguments = CredentialEditFragmentArgs.fromBundle(arguments!!)
        val dataSource = CredentialDatabase.getInstance(application).credentialDao

        val viewModelFactory = CredentialEditViewModelFactory(arguments.credentialId, dataSource)

        val credentialEditViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(CredentialEditViewModel::class.java)

        binding.credentialEditViewModel = credentialEditViewModel

        binding.cancelButton.setOnClickListener {
            onCancel()
        }

        credentialEditViewModel.dataLoaded.observe(this, Observer { dataLoaded ->
            if (dataLoaded) {
                binding.websiteInput.setText(credentialEditViewModel.credential.value?.website)
                binding.usernameInput.setText(credentialEditViewModel.credential.value?.username)
                binding.passwordInput.setText(credentialEditViewModel.credential.value?.password)

                credentialEditViewModel.dataHandled()
            }
        })

        credentialEditViewModel.navigateToCredentialList.observe(this, Observer { navigate ->
            if (navigate) {
                this.findNavController().navigate(CredentialEditFragmentDirections
                        .actionCredentialEditFragmentToCredentialListFragment())

                credentialEditViewModel.doneNavigating()
            }
        })

        binding.saveButton.setOnClickListener {
            val changedCredential = Credential(0L,
                    binding.websiteInput.text.toString(),
                    binding.usernameInput.text.toString(),
                    binding.passwordInput.text.toString())

            credentialEditViewModel.save(changedCredential)
        }

        return binding.root
    }

    private fun onCancel() {
        this.findNavController().navigate(CredentialEditFragmentDirections
                .actionCredentialEditFragmentToCredentialListFragment())
    }
}
