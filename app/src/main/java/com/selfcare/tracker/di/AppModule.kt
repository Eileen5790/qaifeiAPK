package com.selfcare.tracker.di

import android.content.Context
import com.selfcare.tracker.data.local.AppDatabase
import com.selfcare.tracker.data.local.FetishDao
import com.selfcare.tracker.data.local.MediumDao
import com.selfcare.tracker.data.local.RecordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideRecordDao(database: AppDatabase): RecordDao {
        return database.recordDao()
    }

    @Provides
    @Singleton
    fun provideMediumDao(database: AppDatabase): MediumDao {
        return database.mediumDao()
    }

    @Provides
    @Singleton
    fun provideFetishDao(database: AppDatabase): FetishDao {
        return database.fetishDao()
    }
}
