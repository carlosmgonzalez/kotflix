package com.carlosmgonzalez.kotflix.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMoviesList(movies: List<MovieEntity>)

    @Query("select * from movies where type = 'playingNow'")
    fun getPlayingNowMovies(): Flow<List<MovieEntity>>

    @Query("select * from movies where type = 'upcoming'")
    fun getUpcomingMovies(): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovieCast(movieCast: List<MovieCastEntity>)

    @Query("select * from cast_movie where movieId = :id")
    fun getMovieCastById(id: Int): Flow<List<MovieCastEntity>>

    @Query("select * from movies where id = :id")
    fun getMovieDetailById(id: Int): Flow<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTvSeriesList(tvSeries: List<TvSeriesEntity>)

    @Query("select * from tv_series where type = 'popular'")
    fun getPopularTvSeries(): Flow<List<TvSeriesEntity>>

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMovieToFavorites(movie: MovieEntity)

    @Query("select * from movies where isFavorite = 1")
    fun getFavoritesMovies(): Flow<List<MovieEntity>>
}