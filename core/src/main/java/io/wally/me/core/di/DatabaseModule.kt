package io.wally.me.core.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.wally.me.core.data.local.AppDatabase
import io.wally.me.core.data.local.WallpaperDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "wallyme.db"
        ).build()
    }

    @Provides
    fun provideWallpaperDao(database: AppDatabase): WallpaperDao {
        return database.wallpaperDao()
    }
}
