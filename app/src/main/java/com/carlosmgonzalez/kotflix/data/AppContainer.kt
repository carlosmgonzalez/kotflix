package com.carlosmgonzalez.kotflix.data

import android.content.Context
import com.carlosmgonzalez.kotflix.network.KotflixApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val moviesRepository: MoviesRepository
}

class DefaultAppContainer(context: Context): AppContainer {
    private val baseUrl = "https://api.themoviedb.org/3/"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: KotflixApiService by lazy {
        retrofit.create(KotflixApiService::class.java)
    }

    override val moviesRepository: MoviesRepository by lazy {
        MoviesRepository(
            movieDao = KotflixDatabase.getDatabase(context).movieDao(),
            apiService = retrofitService
        )
    }
}