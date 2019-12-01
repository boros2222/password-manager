package com.example.android.passwordmanager.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.OnConflictStrategy


@Dao
interface CredentialDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(credential: Credential): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(credential: Credential)

    @Query("SELECT * from credential_table WHERE id = :id")
    fun get(id: Long): Credential?

    @Query("DELETE FROM credential_table WHERE id = :id")
    fun clear(id: Long)

    @Query("SELECT * FROM credential_table ORDER BY id DESC")
    fun getAllCredentials(): LiveData<List<Credential>>

}
