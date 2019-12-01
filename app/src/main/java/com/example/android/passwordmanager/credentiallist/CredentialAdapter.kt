package com.example.android.passwordmanager.credentiallist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.passwordmanager.R
import com.example.android.passwordmanager.database.Credential

class CredentialAdapter(
        private val deleteListener: (Long) -> Unit,
        private val editListener: (Long) -> Unit
): RecyclerView.Adapter<CredentialAdapter.ViewHolder>() {

    var data = listOf<Credential>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, deleteListener, editListener)
    }

    class ViewHolder private constructor(itemView: View,
                                         private val deleteListener: (Long) -> Unit,
                                         private val editListener: (Long) -> Unit
    ) : RecyclerView.ViewHolder(itemView){

        private val website: TextView = itemView.findViewById(R.id.website)
        private val username: TextView = itemView.findViewById(R.id.username)
        private val password: TextView = itemView.findViewById(R.id.password)
        private val edit: Button = itemView.findViewById(R.id.editButton)
        private val delete: Button = itemView.findViewById(R.id.deleteButton)

        fun bind(item: Credential) {
            website.text = item.website
            username.text = item.username
            password.text = "*".repeat(item.password.length)
            delete.setOnClickListener { deleteListener(item.id) }
            edit.setOnClickListener { editListener(item.id) }
        }

        companion object {
            fun from(parent: ViewGroup,
                     deleteListener: (Long) -> Unit,
                     editListener: (Long) -> Unit): ViewHolder {

                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.credential_item, parent, false)
                return ViewHolder(view, deleteListener, editListener)
            }
        }
    }
}