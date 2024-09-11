package com.carlosmgonzalez.kotflix.network

import com.carlosmgonzalez.kotflix.model.CreditsResult
import com.carlosmgonzalez.kotflix.model.Movie
import com.carlosmgonzalez.kotflix.model.MoviesResult
import com.carlosmgonzalez.kotflix.model.TvSeriesResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface KotflixApiService {
    @GET("discover/movie")
    suspend fun getNowPlayingMoviesList(
        @Query("api_key") apiKey: String
    ): MoviesResult

    @GET("movie/{id}")
    suspend fun getMovieDetailById(
        @Path("id") id: String,
        @Query("api_key") apiKey: String
    ): Movie

    @GET("movie/{id}/credits")
    suspend fun getMovieCastById(
        @Path("id") id: String,
        @Query("api_key") apiKey: String
    ): CreditsResult

    @GET("movie/upcoming")
    suspend fun getUpcomingMoviesList(
        @Query("api_key") apiKey: String
    ): MoviesResult

    @GET("tv/popular")
    suspend fun getTvSeriesPopularList(
        @Query("api_key") apiKey: String
    ): TvSeriesResult

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): MoviesResult
}
