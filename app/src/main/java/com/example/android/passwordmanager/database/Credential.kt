package com.example.android.passwordmanager.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "credential_table")
data class Credential(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0L,

        @ColumnInfo(name = "website")
        var website: String = "",

        @ColumnInfo(name = "username")
        var username: String = "",

        @ColumnInfo(name = "password")
        var password: String = ""
)