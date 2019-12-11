package com.example.android.passwordmanager.credentiallist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.passwordmanager.R
import com.example.android.passwordmanager.database.Credential
import com.example.android.passwordmanager.databinding.CredentialItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class CredentialAdapter(
        private val deleteListener: (Long) -> Unit,
        private val editListener: (Long) -> Unit
): ListAdapter<DataItem, RecyclerView.ViewHolder>(CredentialDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<Credential>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.CredentialItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.CredentialItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val credentialItem = getItem(position) as DataItem.CredentialItem
                holder.bind(credentialItem.credential)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent, deleteListener, editListener)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    class TextViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.credential_header, parent, false)
                return TextViewHolder(view)
            }
        }
    }

    class ViewHolder private constructor(val binding: CredentialItemBinding,
                                         private val deleteListener: (Long) -> Unit,
                                         private val editListener: (Long) -> Unit
    ) : RecyclerView.ViewHolder(binding.root){

        private val edit: Button = binding.editButton
        private val delete: Button = binding.deleteButton

        fun bind(item: Credential) {
            binding.credential = item
            binding.executePendingBindings()
            delete.setOnClickListener { deleteListener(item.id) }
            edit.setOnClickListener { editListener(item.id) }
        }

        companion object {
            fun from(parent: ViewGroup,
                     deleteListener: (Long) -> Unit,
                     editListener: (Long) -> Unit): ViewHolder {

                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CredentialItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, deleteListener, editListener)
            }
        }
    }
}

class CredentialDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

sealed class DataItem {

    abstract val id: Long

    data class CredentialItem(val credential: Credential): DataItem() {
        override val id = credential.id
    }

    object Header: DataItem() {
        override val id = Long.MIN_VALUE
    }
}