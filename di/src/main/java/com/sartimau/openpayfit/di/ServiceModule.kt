package com.sartimau.openpayfit.di

import com.sartimau.openpayfit.data.service.FirebaseServiceImpl
import com.sartimau.openpayfit.data.service.TheMovieDBServiceImpl
import com.sartimau.openpayfit.data.service.api.TheMovieDBApi
import com.sartimau.openpayfit.domain.service.FirebaseService
import com.sartimau.openpayfit.domain.service.TheMovieDBService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    fun provideTheMovieDBService(theMovieDBApi: TheMovieDBApi): TheMovieDBService = TheMovieDBServiceImpl(theMovieDBApi)

    @Provides
    fun provideFirebaseService(): FirebaseService = FirebaseServiceImpl()
}
