package com.example.fragmentjetpacknavigation.db

import android.content.Context
import android.os.strictmode.InstanceCountViolation
import android.provider.ContactsContract
import android.telecom.Call
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.fragmentjetpacknavigation.Contact
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.security.AccessControlContext

@Database(entities = [ContactEntity::class], version = 1, exportSchema = false)
public abstract class ContactRoomDatabase: RoomDatabase() {
    abstract val contactDatabaseDao: ContactDao

    companion object {
        @Volatile
        private var INSTANCE: ContactRoomDatabase? = null

        fun getInstance(context: Context): ContactRoomDatabase{
            synchronized(this) {
                var instance = INSTANCE

                if(instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext,
                    ContactRoomDatabase::class.java,
                    "contact_database")
                        .fallbackToDestructiveMigration()
                        .addCallback(object: Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                loadContacts(context)
                            }
                        })
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

        private fun loadContacts(context: Context) {
            Log.d("ContactProvider", "loadContacts() invoked!")
            GlobalScope.launch {
                val cursor = context.contentResolver?.query(
                    ContactsContract.Contacts.CONTENT_URI,
                    null,
                    null,
                    null
                )
                val contacts = ArrayList<ContactEntity>()
                while (cursor?.moveToNext() == true) {
                    val contact = Contact(
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)),
                        arrayListOf()
                    )
                    val hasPhoneNumber =
                        cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                    if (hasPhoneNumber > 0) {
                        val id: String =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        val cur = context.contentResolver?.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf(id),
                            null
                        )
                        while (cur?.moveToNext() == true) {
                            val phoneNumber: String =
                                cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            contact.phoneNumbers.add(phoneNumber)
                        }
                        cur?.close()
                    }

                    contacts.add(ContactEntity(0, contact.name, contact.phoneNumbers[0]))
                }
                cursor?.close()

                contacts.forEach {  INSTANCE?.contactDatabaseDao?.insert(it) }
                Log.d("ContactProvider", "loadContacts() stopped!")
            }
        }


    }

}