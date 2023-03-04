package com.sartimau.openpayfit.data.service.api

import com.sartimau.openpayfit.data.service.model.PersonResponse
import retrofit2.Call
import retrofit2.http.GET

interface TheMovieDBApi {

    @GET("/3/person/popular")
    fun getPopularPersonList(): Call<PersonResponse>
}
