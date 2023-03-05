package com.sartimau.openpayfit.data.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "movie")
class MovieEntity(
    @PrimaryKey val id: Int,
    val adult: Boolean,
    val backdropPath: String,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int
)

@Entity(tableName = "popular_movie")
class PopularMovieEntity(
    @PrimaryKey val popularMovieId: Int,
    @Embedded val movieEntity: MovieEntity
)

@Entity(tableName = "top_rated_movie")
class TopRatedMovieEntity(
    @PrimaryKey val topRatedMovieId: Int,
    @Embedded val movieEntity: MovieEntity
)

@Entity(tableName = "recommended_movie")
class RecommendedMovieEntity(
    @PrimaryKey val recommendedMovieId: Int,
    val originalMovieId: Int,
    @Embedded val movieEntity: MovieEntity
)

data class PopularMovieEntityComplete(
    @Embedded val movieEntity: MovieEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "popularMovieId"
    )
    val popularMovieEntity: PopularMovieEntity
)

data class TopRatedMovieEntityComplete(
    @Embedded val movieEntity: MovieEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "topRatedMovieId"
    )
    val topRatedMovieEntity: TopRatedMovieEntity
)

data class RecommendedMovieEntityComplete(
    @Embedded val movieEntity: MovieEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "recommendedMovieId"
    )
    val recommendedMovieEntity: RecommendedMovieEntity
)
