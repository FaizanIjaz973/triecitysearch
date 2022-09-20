package com.example.triecitysearch.dependencyinjection

import android.content.Context
import com.example.triecitysearch.repo.Repo
import com.example.triecitysearch.repo.RepoImpl
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
    fun providesRepo(@ApplicationContext context: Context) = RepoImpl(context) as Repo
}