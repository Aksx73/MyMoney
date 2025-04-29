package com.absut.cash.management.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.absut.cash.management.data.model.Book
import com.absut.cash.management.util.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM books_table ORDER BY created DESC")
    fun getAllBooks(): Flow<List<Book>>

    @Query("SELECT * FROM books_table WHERE id=:bookId")
    fun getBookById(bookId: Int): Flow<Book>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBook(book: Book)

    @Update
    suspend fun updateBook(book: Book)

    @Query("UPDATE books_table SET title=:title WHERE id=:bookId")
    suspend fun updateBookTitle(bookId: Int, title: String)

    @Delete
    suspend fun deleteBook(book: Book)


    @Query("DELETE FROM entry_table WHERE bookId=:bookId")
    suspend fun deleteBookEntries(bookId: Int)

    /**
     * No need to delete all entries separately as we have added foreign key constraint in Entry table
     * to remove all entries if associated book ifd is deleted
     * */
    @Transaction
    suspend fun deleteBookData(book: Book) {
        deleteBook(book)
        deleteBookEntries(book.id)
    }

}