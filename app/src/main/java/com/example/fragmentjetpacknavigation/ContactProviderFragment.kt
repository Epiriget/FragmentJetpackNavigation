package com.example.fragmentjetpacknavigation

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fragmentjetpacknavigation.db.ContactEntity
import com.example.fragmentjetpacknavigation.db.ContactRoomDatabase
import com.example.fragmentjetpacknavigation.db.ContactViewModel
import com.example.fragmentjetpacknavigation.db.ContactViewModelFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ContactProviderFragment : Fragment() {
    private lateinit var contactViewModel: ContactViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact_provider_list, container, false)


        val application = requireNotNull(this.activity).application
        val dataSource = ContactRoomDatabase.getInstance(
            application
        ).contactDatabaseDao
        val viewModelFactory = ContactViewModelFactory(dataSource, application)
        contactViewModel = ViewModelProvider(this, viewModelFactory)
            .get(ContactViewModel::class.java)


        val contactsAdapter = ContactRecyclerViewAdapter()
        contactViewModel.contacts.observe(viewLifecycleOwner, Observer {
            contactsAdapter.setContacts(it)
            if (view is RecyclerView) {
                with(view) {
                    layoutManager = LinearLayoutManager(context)
                    adapter = contactsAdapter
                }
            }
        })
        return view
    }
}
