package com.sartimau.openpayfit.domain.entity

data class PopularPerson(
    val id: Int,
    val adult: Boolean,
    val gender: Int,
    val knownFor: List<KnownFor>,
    val knownForDepartment: String,
    val name: String,
    val popularity: Double,
    val profilePath: String
)

data class KnownFor(
    val id: Int,
    val adult: Boolean,
    val backdropPath: String,
    val firstAirDate: String,
    val mediaType: String,
    val name: String,
    val originalLanguage: String,
    val originalName: String,
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
