package com.absut.cash.management.data.db

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.absut.cash.management.data.db.dao.CategoryDao
import com.absut.cash.management.data.model.DefaultCategories
import com.absut.cash.management.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class DatabaseCallback @Inject constructor(
    private val categoryDao: Provider<CategoryDao>,
    @ApplicationScope private val applicationScope: CoroutineScope
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        applicationScope.launch(Dispatchers.IO) {
            categoryDao.get().apply {
                DefaultCategories.defaults.forEach { category ->
                    add(category)
                }
            }
        }
    }
}