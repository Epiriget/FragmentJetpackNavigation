package com.example.fragmentjetpacknavigation.db

import android.app.Application
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fragmentjetpacknavigation.Contact
import com.example.fragmentjetpacknavigation.MainActivity
import kotlinx.coroutines.launch


// Todo: Ask: Why app context is not the best solution in VM?
//  Lifecycle of context is whole app lasting and VM's too

class ContactViewModel (val database: ContactDao, application: Application) : AndroidViewModel(application) {
    val contacts = database.getAllContacts()

    init {

    }

    suspend fun insertAll(contacts: List<ContactEntity>) {
        contacts.forEach { insert(it) }
        Log.d("ContactViewModel", "${contacts.size}")
    }

    private suspend fun insert(contact: ContactEntity) {
        database.insert(contact)
    }

    suspend fun clear() {
        database.clear()
    }

}