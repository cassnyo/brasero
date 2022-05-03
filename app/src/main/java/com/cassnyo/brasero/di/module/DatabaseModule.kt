package com.cassnyo.brasero.di.module

import android.content.Context
import androidx.room.Room
import com.cassnyo.brasero.data.database.BraseroDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideBraseroDatabase(
        @ApplicationContext appContext: Context
    ): BraseroDatabase {
        return Room.databaseBuilder(
            appContext,
            BraseroDatabase::class.java,
            "brasero-db"
        ).build()
    }

}