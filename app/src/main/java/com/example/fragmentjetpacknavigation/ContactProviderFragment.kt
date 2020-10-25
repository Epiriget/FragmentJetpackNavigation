package com.example.fragmentjetpacknavigation

import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ContactProviderFragment : Fragment() {
    private var contacts = ArrayList<Contact>()
    companion object {
        private var REQUEST_CODE_READ_CONTACTS: Int = 1
        private var READ_CONTACTS_GRANTED: Boolean = false
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact_provider_list, container, false)


        val hasContactsPermission: Int = ContextCompat.checkSelfPermission(
            requireContext(), android.Manifest.permission.READ_CONTACTS
        )
        if(hasContactsPermission == PackageManager.PERMISSION_GRANTED) {
            READ_CONTACTS_GRANTED = true
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(android.Manifest.permission.READ_CONTACTS),
                REQUEST_CODE_READ_CONTACTS
            )
        }

        if(READ_CONTACTS_GRANTED) {
            loadContacts()
        }

        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = ContactRecyclerViewAdapter(contacts)
            }
        }
        return view
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            REQUEST_CODE_READ_CONTACTS -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    READ_CONTACTS_GRANTED = true
                }
            }
        }
        if(READ_CONTACTS_GRANTED) {
            loadContacts()
        }
    }

    private fun loadContacts() {
        val cursor =  context?.contentResolver?.query(ContactsContract.Contacts.CONTENT_URI, null, null, null)
        while (cursor?.moveToNext() == true) {
            val contact = Contact(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)), arrayListOf())
            val hasPhoneNumber = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
            if(hasPhoneNumber > 0) {
                val id:String = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                val cur = context?.contentResolver?.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", arrayOf(id), null)
                while (cur?.moveToNext() == true) {
                    val phoneNumber: String = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    contact.phoneNumbers.add(phoneNumber)
                }
                cur?.close()
            }
            contacts.add(contact)
        }
        cursor?.close()
    }
}
