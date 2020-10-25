package com.example.fragmentjetpacknavigation

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_contact_provider.view.*

class ContactRecyclerViewAdapter(
    private val mValues: List<Contact>
) : RecyclerView.Adapter<ContactRecyclerViewAdapter.ViewHolder>() {


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

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        private val mNameView: TextView = mView.contact_name
        private val mFirstPhoneView: TextView = mView.contact_phone_1
        private val mSecondPhoneView: TextView = mView.contact_phone_2

        fun bind(contact: Contact) {
            mNameView.text = contact.name
            mFirstPhoneView.text = if(contact.phoneNumbers.size > 0) contact.phoneNumbers[0] else ""
            mSecondPhoneView.text = if(contact.phoneNumbers.size > 1) contact.phoneNumbers[1] else ""
        }
    }
}
