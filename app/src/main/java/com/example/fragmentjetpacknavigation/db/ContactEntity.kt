package com.example.fragmentjetpacknavigation.db

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts_table")
data class ContactEntity(
    @Nullable
    @PrimaryKey(autoGenerate = true)
    val contactId:Long,
    @ColumnInfo(name = "name")
    val name:String,
    @ColumnInfo(name = "number")
    val phoneNumber: String)