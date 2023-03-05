package com.sartimau.openpayfit.data.service.api

import com.sartimau.openpayfit.data.service.model.MoviePagingResponse
import com.sartimau.openpayfit.data.service.model.PersonPagingResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface TheMovieDBApi {

    @GET("/3/person/popular")
    fun getPopularPersonList(): Call<PersonPagingResponse>

    @GET("/3/movie/popular")
    fun getPopularMovieList(): Call<MoviePagingResponse>

    @GET("/3/movie/top_rated")
    fun getTopRatedMovieList(): Call<MoviePagingResponse>

    @GET("/3/movie/{movie_id}/recommendations")
    fun getRecommendedMoviesListById(@Path("movie_id") movieId: Int,): Call<MoviePagingResponse>
}
