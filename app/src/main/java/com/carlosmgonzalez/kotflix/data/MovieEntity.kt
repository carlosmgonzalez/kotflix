package com.carlosmgonzalez.kotflix.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val type: String,
    val releaseDate: String,
    val posterPath: String? = null,
    val backdropPath: String? = null,
    val voteAverage: Float,
    val overview: String,
    val isFavorite: Boolean = false
)

@Entity(tableName = "cast_movie")
data class MovieCastEntity(
    @PrimaryKey
    val id: Int,
    val movieId: Int,
    val name: String,
    val profilePath: String? = null,
    val character: String
)

data class MovieAndCast(
    @Embedded val movie: MovieEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "movieId"
    )
    val movieCast: List<MovieCastEntity>
)

@Entity(tableName = "tv_series")
data class TvSeriesEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val type: String,
    val overview: String,
    val firstAirDate: String,
    val posterPath: String? = null,
    val backdropPath: String? = null,
    val voteAverage: Float,
    val isFavorite: Boolean = false
)