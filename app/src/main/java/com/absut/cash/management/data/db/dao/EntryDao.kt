package com.absut.cash.management.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.absut.cash.management.data.model.Entry
import com.absut.cash.management.data.model.EntryWithCategory
import com.absut.cash.management.util.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface EntryDao {

    @Query("SELECT * FROM entry_table WHERE bookId=:bookId ORDER BY updatedAt DESC")
    fun getAllEntriesOfBook(bookId: Int): Flow<List<Entry>>

    /*@Query("""
    SELECT e.*,
           c.id as category_id, c.name as category_name, c.iconId as category_iconId
    FROM entry_table e
    LEFT JOIN category_table c ON e.categoryId = c.id
    WHERE e.bookId = :bookId 
    ORDER BY e.updatedAt DESC
""")*/

    @Transaction
    @Query("""
        SELECT e.*, c.id AS category_id, c.name AS category_name, c.iconId AS category_iconId, isActive AS category_isActive
        FROM entry_table AS e
        LEFT JOIN category_table AS c ON e.categoryId = c.id 
        WHERE e.bookId = :bookId 
        ORDER BY e.updatedAt DESC
    """)
    fun getEntriesWithCategory(bookId: Int): Flow<List<EntryWithCategory>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addEntry(entry: Entry)

    @Update
    suspend fun updateEntry(entry: Entry)

    @Delete
    suspend fun deleteEntry(entry: Entry)

    @Query("DELETE FROM entry_table WHERE bookId=:bookId")
    suspend fun deleteAllEntriesOfBook(bookId: Int)


}