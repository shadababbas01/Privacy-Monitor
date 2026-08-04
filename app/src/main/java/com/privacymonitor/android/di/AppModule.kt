package com.privacymonitor.android.di

import com.privacymonitor.android.core.util.DispatcherProvider
import com.privacymonitor.android.core.util.StandardDispatchers
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindDispatcherProvider(dispatchers: StandardDispatchers): DispatcherProvider
}
