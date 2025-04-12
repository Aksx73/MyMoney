package com.absut.cash.management.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.absut.cash.management.data.db.dao.BookDao
import com.absut.cash.management.data.db.dao.CategoryDao
import com.absut.cash.management.data.db.dao.EntryDao
import com.absut.cash.management.data.model.Book
import com.absut.cash.management.data.model.Category
import com.absut.cash.management.data.model.Entry


@Database(entities = [Book::class, Category::class, Entry::class], version = 1, exportSchema = true)
abstract class AccountDatabase: RoomDatabase() {
    abstract fun BookDao(): BookDao
    abstract fun CategoryDao(): CategoryDao
    abstract fun EntryDao(): EntryDao
}