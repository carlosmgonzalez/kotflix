package com.carlosmgonzalez.kotflix.model

import com.carlosmgonzalez.kotflix.data.MovieEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoviesResult(
    val page: Int,
    val results: List<Movie>
)

@Serializable
data class Movie(
    val id: Int,
    val title: String,
    @SerialName("release_date")
    val releaseDate: String,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    @SerialName("vote_average")
    val voteAverage: Float,
    val overview: String
)

fun Movie.toMovieEntity(type: String, isFavorite: Boolean): MovieEntity {
    return MovieEntity(
        id = this.id,
        title = this.title,
        releaseDate = this.releaseDate,
        posterPath = this.posterPath,
        backdropPath = this.backdropPath,
        voteAverage = this.voteAverage,
        overview = this.overview,
        type = type,
        isFavorite = isFavorite
    )
}

@Serializable
data class CreditsResult(
    val id: Int,
    val cast: List<Cast>
)

@Serializable
data class Cast(
    val id: Int,
    val name: String,
    @SerialName("profile_path")
    val profilePath: String?,
    val character: String
)

@Serializable
data class TvSeriesResult(
    val page: Int,
    val results: List<TvSeries>
)

@Serializable
data class TvSeries(
    val id: Int,
    val name: String,
    val overview: String,
    @SerialName("first_air_date")
    val firstAirDate: String,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    @SerialName("vote_average")
    val voteAverage: Float,
)