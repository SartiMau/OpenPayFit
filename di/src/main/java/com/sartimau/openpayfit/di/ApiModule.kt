package com.sartimau.openpayfit.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sartimau.openpayfit.data.BuildConfig
import com.sartimau.openpayfit.data.service.api.TheMovieDBApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    private const val AUTHORIZATION = "Authorization"

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    fun provideOkHttpClient(chuckerInterceptor: ChuckerInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val defaultRequest = chain.request()
                val defaultHttpUrl = defaultRequest.url
                val httpUrl = defaultHttpUrl.newBuilder()
                    .build()
                val requestBuilder = defaultRequest.newBuilder()
                    .header(AUTHORIZATION, BuildConfig.AUTHORIZATION_VALUE)
                    .url(httpUrl)
                chain.proceed(requestBuilder.build())
            }
            .addInterceptor(chuckerInterceptor)
            .build()
    }

    @Provides
    @ExperimentalSerializationApi
    fun provideConverterFactory(): Converter.Factory = json.asConverterFactory("application/json".toMediaType())

    @Provides
    fun provideTheMovieDBApi(okHttpClient: OkHttpClient, converterFactory: Converter.Factory): TheMovieDBApi {
        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.MOVIES_BASE_URL)
            .addConverterFactory(converterFactory)
            .build()

        return retrofit.create(TheMovieDBApi::class.java)
    }

    @Provides
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        return ChuckerInterceptor.Builder(context)
            .collector(ChuckerCollector(context))
            .maxContentLength(250000L)
            .redactHeaders(emptySet())
            .alwaysReadResponseBody(false)
            .build()
    }
}
