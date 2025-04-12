package com.absut.cash.management.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.absut.cash.management.data.model.Entry
import com.absut.cash.management.util.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface EntryDao {

    @Query("SELECT * FROM "+ Constants.ENTRY_TABLE_NAME+ " WHERE book_id=:bookId ORDER BY updated_at DESC")
    fun getAllEntriesOfBook(bookId: Int): Flow<List<Entry>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addEntry(entry: Entry)

    @Update
    suspend fun updateEntry(entry: Entry)

    @Delete
    suspend fun deleteEntry(entry: Entry)

}