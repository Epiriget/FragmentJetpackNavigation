package com.example.fragmentjetpacknavigation

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.fragmentjetpacknavigation.db.ContactEntity
import kotlinx.android.synthetic.main.fragment_contact_provider.view.*

class ContactRecyclerViewAdapter: RecyclerView.Adapter<ContactRecyclerViewAdapter.ViewHolder>() {

    private var mValues: List<ContactEntity> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_contact_provider, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = mValues.size

    public fun setContacts(contacts: List<ContactEntity>) {
        mValues = contacts
        notifyDataSetChanged()
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        private val mNameView: TextView = mView.contact_name
        private val mFirstPhoneView: TextView = mView.contact_phone_1

        fun bind(contact: ContactEntity) {
            mNameView.text = contact.name
            mFirstPhoneView.text = contact.phoneNumber
        }
    }
}
