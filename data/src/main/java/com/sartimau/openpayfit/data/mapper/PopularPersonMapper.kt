package com.sartimau.openpayfit.data.mapper

import com.sartimau.openpayfit.data.database.entity.KnownForEntity
import com.sartimau.openpayfit.data.database.entity.PopularPersonEntity
import com.sartimau.openpayfit.data.database.entity.PopularPersonEntityWithKnownForEntity
import com.sartimau.openpayfit.data.service.model.KnownForResponse
import com.sartimau.openpayfit.data.service.model.PersonResponse
import com.sartimau.openpayfit.data.service.model.PopularPersonResponse
import com.sartimau.openpayfit.domain.entity.KnownFor
import com.sartimau.openpayfit.domain.entity.PopularPerson

fun PersonResponse.mapToLocalPopularPersonList(): List<PopularPerson> = results.map { it.mapToLocalPopularPerson() }

private fun PopularPersonResponse.mapToLocalPopularPerson() =
    PopularPerson(
        id = id,
        adult = adult,
        gender = gender,
        knownFor = knownFor.map { it.mapToLocalKnownFor() },
        knownForDepartment = knownForDepartment,
        name = name,
        popularity = popularity,
        profilePath = profilePath
    )

private fun KnownForResponse.mapToLocalKnownFor() =
    KnownFor(
        id = id,
        adult = adult,
        backdropPath = backdropPath,
        firstAirDate = firstAirDate,
        mediaType = mediaType,
        name = name,
        originalLanguage = originalLanguage,
        originalName = originalName,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount
    )

fun PopularPerson.mapToDataBasePopularPerson() =
    PopularPersonEntity(
        id = id,
        adult = adult,
        gender = gender,
        knownForDepartment = knownForDepartment,
        name = name,
        popularity = popularity,
        profilePath = profilePath
    )

fun KnownFor.mapToDataBaseKnownFor(userId: Int) =
    KnownForEntity(
        id = id,
        userId = userId,
        adult = adult,
        backdropPath = backdropPath,
        firstAirDate = firstAirDate,
        mediaType = mediaType,
        name = name,
        originalLanguage = originalLanguage,
        originalName = originalName,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount
    )

fun List<PopularPersonEntityWithKnownForEntity>.mapToLocalPopularPersonList(): List<PopularPerson> = this.map { it.mapToLocalPopularPerson() }

private fun PopularPersonEntityWithKnownForEntity.mapToLocalPopularPerson() =
    PopularPerson(
        id = popularPersonEntity.id,
        adult = popularPersonEntity.adult,
        gender = popularPersonEntity.gender,
        knownFor = knownForEntity.map { it.mapToLocalKnownFor() },
        knownForDepartment = popularPersonEntity.knownForDepartment,
        name = popularPersonEntity.name,
        popularity = popularPersonEntity.popularity,
        profilePath = popularPersonEntity.profilePath
    )

private fun KnownForEntity.mapToLocalKnownFor() =
    KnownFor(
        id = id,
        adult = adult,
        backdropPath = backdropPath,
        firstAirDate = firstAirDate,
        mediaType = mediaType,
        name = name,
        originalLanguage = originalLanguage,
        originalName = originalName,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount
    )
