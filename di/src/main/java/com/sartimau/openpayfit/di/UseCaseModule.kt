package com.sartimau.openpayfit.di

import com.sartimau.openpayfit.domain.database.TheMovieDBRepository
import com.sartimau.openpayfit.domain.service.TheMovieDBService
import com.sartimau.openpayfit.domain.usecase.GetPopularPersonListUseCase
import com.sartimau.openpayfit.domain.usecase.GetPopularPersonListUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetCharacterListUseCase(
        theMovieDBService: TheMovieDBService,
        theMovieDBRepository: TheMovieDBRepository
    ): GetPopularPersonListUseCase =
        GetPopularPersonListUseCaseImpl(theMovieDBService, theMovieDBRepository)
}
