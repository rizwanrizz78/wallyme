package io.wally.me.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.wally.me.core.data.repository.FirebaseWallpaperRepository
import io.wally.me.core.data.repository.WallpaperRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWallpaperRepository(
        firebaseWallpaperRepository: FirebaseWallpaperRepository
    ): WallpaperRepository
}
