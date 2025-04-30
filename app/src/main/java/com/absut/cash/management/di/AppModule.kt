package com.absut.cash.management.di

import android.content.Context
import androidx.room.Room
import com.absut.cash.management.data.repository.BookRepoImpl
import com.absut.cash.management.data.repository.BookRepository
import com.absut.cash.management.data.repository.CategoryRepoImpl
import com.absut.cash.management.data.repository.CategoryRepository
import com.absut.cash.management.data.repository.EntryRepoImpl
import com.absut.cash.management.data.repository.EntryRepository
import com.absut.cash.management.data.db.AccountDatabase
import com.absut.cash.management.data.db.DatabaseCallback
import com.absut.cash.management.data.db.dao.BookDao
import com.absut.cash.management.data.db.dao.CategoryDao
import com.absut.cash.management.data.db.dao.EntryDao
import com.absut.cash.management.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext appContext: Context,
        callback: DatabaseCallback
    ): AccountDatabase {
        return Room.databaseBuilder(
            appContext,
            AccountDatabase::class.java,
            Constants.DATABASE_NAME
        )
            .addCallback(callback)
            .build()
    }

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

    @Provides
    @Singleton
    fun provideBookDao(db: AccountDatabase): BookDao {
        return db.BookDao()
    }

    @Provides
    @Singleton
    fun provideCategoryDao(db: AccountDatabase): CategoryDao {
        return db.CategoryDao()
    }

    @Provides
    @Singleton
    fun provideEntryDao(db: AccountDatabase): EntryDao {
        return db.EntryDao()
    }

    @Provides
    @Singleton
    fun provideBookRepository(bookDao: BookDao): BookRepository {
        return BookRepoImpl(bookDao)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(categoryDao: CategoryDao): CategoryRepository {
        return CategoryRepoImpl(categoryDao)
    }

    @Provides
    @Singleton
    fun provideEntryRepository(
        entryDao: EntryDao,
        bookDao: BookDao,
        categoryDao: CategoryDao
    ): EntryRepository {
        return EntryRepoImpl(entryDao, bookDao, categoryDao)
    }
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope
