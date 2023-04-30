package com.task.azercell.di

import android.content.Context
import com.task.azercell.data.PreferencesHelper
import com.task.azercell.data.PreferencesHelperFacade
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    @Provides
    @Singleton
    fun provideSharedPreferencesFacade(
        @ApplicationContext context: Context
    ): PreferencesHelperFacade = PreferencesHelper(context)

}