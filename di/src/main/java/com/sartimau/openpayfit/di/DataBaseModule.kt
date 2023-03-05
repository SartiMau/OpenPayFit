package com.sartimau.openpayfit.di

import android.content.Context
import androidx.room.Room
import com.sartimau.openpayfit.data.database.TheMovieDBDB
import com.sartimau.openpayfit.data.database.TheMovieDBRepositoryImpl
import com.sartimau.openpayfit.data.database.dao.KnownForDao
import com.sartimau.openpayfit.data.database.dao.PopularMovieDao
import com.sartimau.openpayfit.data.database.dao.PopularPersonDao
import com.sartimau.openpayfit.data.database.dao.RecommendedMovieDao
import com.sartimau.openpayfit.data.database.dao.TopRatedMovieDao
import com.sartimau.openpayfit.domain.database.TheMovieDBRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    private const val DB = "TheMovieDBRepository"

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): TheMovieDBDB {
        return Room
            .databaseBuilder(context, TheMovieDBDB::class.java, DB)
            .build()
    }

    @Provides
    @Singleton
    fun providePopularPersonDao(theMovieDBDB: TheMovieDBDB): PopularPersonDao = theMovieDBDB.popularPersonDao()

    @Provides
    @Singleton
    fun provideKnownForDao(theMovieDBDB: TheMovieDBDB): KnownForDao = theMovieDBDB.knownForDao()

    @Provides
    @Singleton
    fun providePopularMovieDao(theMovieDBDB: TheMovieDBDB): PopularMovieDao = theMovieDBDB.popularMovieDao()

    @Provides
    @Singleton
    fun provideTopRatedMovieDao(theMovieDBDB: TheMovieDBDB): TopRatedMovieDao = theMovieDBDB.topRatedMovieDao()

    @Provides
    @Singleton
    fun provideRecommendedMovieDao(theMovieDBDB: TheMovieDBDB): RecommendedMovieDao = theMovieDBDB.recommendedMovieDao()

    @Provides
    fun provideTheMovieDBRepository(
        popularPersonDao: PopularPersonDao,
        knownForDao: KnownForDao,
        popularMovieDao: PopularMovieDao,
        topRatedMovieDao: TopRatedMovieDao,
        recommendedMovieDao: RecommendedMovieDao
    ): TheMovieDBRepository =
        TheMovieDBRepositoryImpl(
            popularPersonDao = popularPersonDao,
            knownForDao = knownForDao,
            popularMovieDao = popularMovieDao,
            topRatedMovieDao = topRatedMovieDao,
            recommendedMovieDao = recommendedMovieDao
        )
}
