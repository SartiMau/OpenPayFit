package com.sartimau.openpayfit.di

import com.sartimau.openpayfit.domain.database.TheMovieDBRepository
import com.sartimau.openpayfit.domain.service.FirebaseService
import com.sartimau.openpayfit.domain.service.TheMovieDBService
import com.sartimau.openpayfit.domain.usecase.GetLocationsUseCase
import com.sartimau.openpayfit.domain.usecase.GetLocationsUseCaseImpl
import com.sartimau.openpayfit.domain.usecase.GetPopularMovieListUseCase
import com.sartimau.openpayfit.domain.usecase.GetPopularMovieListUseCaseImpl
import com.sartimau.openpayfit.domain.usecase.GetPopularPersonListUseCase
import com.sartimau.openpayfit.domain.usecase.GetPopularPersonListUseCaseImpl
import com.sartimau.openpayfit.domain.usecase.GetRecommendedMoviesListByIdUseCase
import com.sartimau.openpayfit.domain.usecase.GetRecommendedMoviesListByIdUseCaseImpl
import com.sartimau.openpayfit.domain.usecase.GetTopRatedMovieListUseCase
import com.sartimau.openpayfit.domain.usecase.GetTopRatedMovieListUseCaseImpl
import com.sartimau.openpayfit.domain.usecase.SaveImagesUseCase
import com.sartimau.openpayfit.domain.usecase.SaveImagesUseCaseImpl
import com.sartimau.openpayfit.domain.usecase.SaveLocationUseCase
import com.sartimau.openpayfit.domain.usecase.SaveLocationUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetPopularPersonListUseCase(
        theMovieDBService: TheMovieDBService,
        theMovieDBRepository: TheMovieDBRepository
    ): GetPopularPersonListUseCase =
        GetPopularPersonListUseCaseImpl(theMovieDBService, theMovieDBRepository)

    @Provides
    fun provideGetPopularMovieListUseCase(
        theMovieDBService: TheMovieDBService,
        theMovieDBRepository: TheMovieDBRepository
    ): GetPopularMovieListUseCase =
        GetPopularMovieListUseCaseImpl(theMovieDBService, theMovieDBRepository)

    @Provides
    fun provideGetTopRatedMovieListUseCase(
        theMovieDBService: TheMovieDBService,
        theMovieDBRepository: TheMovieDBRepository
    ): GetTopRatedMovieListUseCase =
        GetTopRatedMovieListUseCaseImpl(theMovieDBService, theMovieDBRepository)

    @Provides
    fun provideGetRecommendedMoviesListByIdUseCase(
        theMovieDBService: TheMovieDBService,
        theMovieDBRepository: TheMovieDBRepository
    ): GetRecommendedMoviesListByIdUseCase =
        GetRecommendedMoviesListByIdUseCaseImpl(theMovieDBService, theMovieDBRepository)

    @Provides
    fun provideSaveLocationUseCase(firebaseService: FirebaseService): SaveLocationUseCase = SaveLocationUseCaseImpl(firebaseService)

    @Provides
    fun provideGetLocationsUseCase(firebaseService: FirebaseService): GetLocationsUseCase = GetLocationsUseCaseImpl(firebaseService)

    @Provides
    fun provideSaveImagesUseCase(firebaseService: FirebaseService): SaveImagesUseCase = SaveImagesUseCaseImpl(firebaseService)
}
