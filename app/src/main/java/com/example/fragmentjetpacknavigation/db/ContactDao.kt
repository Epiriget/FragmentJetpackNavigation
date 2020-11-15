package com.example.fragmentjetpacknavigation.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ContactDao {
    @Query("SELECT * FROM contacts_table")
    fun getAllContacts(): LiveData<List<ContactEntity>>

    @Query("DELETE FROM contacts_table")
    suspend fun clear()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: ContactEntity)
}