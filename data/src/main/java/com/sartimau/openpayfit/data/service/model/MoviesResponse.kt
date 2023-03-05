package com.sartimau.openpayfit.data.service.model

import com.sartimau.openpayfit.domain.utils.EMPTY_STRING
import com.sartimau.openpayfit.domain.utils.ZERO_DOUBLE
import com.sartimau.openpayfit.domain.utils.ZERO_INT
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoviePagingResponse(
    @SerialName("page") val page: Int,
    @SerialName("results") val results: List<MovieResponse>,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_results") val totalResults: Int
) : java.io.Serializable

@Serializable
data class MovieResponse(
    @SerialName("id") val id: Int,
    @SerialName("adult") val adult: Boolean = false,
    @SerialName("backdrop_path") val backdropPath: String = EMPTY_STRING,
    @SerialName("original_language") val originalLanguage: String = EMPTY_STRING,
    @SerialName("original_title") val originalTitle: String = EMPTY_STRING,
    @SerialName("overview") val overview: String = EMPTY_STRING,
    @SerialName("popularity") val popularity: Double = ZERO_DOUBLE,
    @SerialName("poster_path") val posterPath: String = EMPTY_STRING,
    @SerialName("release_date") val releaseDate: String = EMPTY_STRING,
    @SerialName("title") val title: String = EMPTY_STRING,
    @SerialName("video") val video: Boolean = false,
    @SerialName("vote_average") val voteAverage: Double = ZERO_DOUBLE,
    @SerialName("vote_count") val voteCount: Int = ZERO_INT
) : java.io.Serializable
