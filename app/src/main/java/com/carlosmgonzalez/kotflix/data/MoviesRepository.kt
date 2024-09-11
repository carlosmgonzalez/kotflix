package com.carlosmgonzalez.kotflix.data

import android.util.Log
import com.carlosmgonzalez.kotflix.model.Cast
import com.carlosmgonzalez.kotflix.model.Movie
import com.carlosmgonzalez.kotflix.model.MoviesResult
import com.carlosmgonzalez.kotflix.network.KotflixApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.io.IOException

class MoviesRepository(
    private val movieDao: MovieDao,
    private val apiService: KotflixApiService
) {
    val playingNowMovies: Flow<List<MovieEntity>> = movieDao.getPlayingNowMovies()
    val tvSeriesPopular: Flow<List<TvSeriesEntity>> = movieDao.getPopularTvSeries()
    val upcomingMovies: Flow<List<MovieEntity>> = movieDao.getUpcomingMovies()
    val favoritesMovies: Flow<List<MovieEntity>> = movieDao.getFavoritesMovies()

    fun getMovieDetailById(id: String): Flow<MovieEntity> =
        movieDao.getMovieDetailById(id = id.toInt())

    fun getMovieCastById(id: String): Flow<List<MovieCastEntity>> =
        movieDao.getMovieCastById(id = id.toInt())

    suspend fun addMovieToFavorites(movie: MovieEntity) {
        movieDao.addMovieToFavorites(movie)
    }

    suspend fun fetchSearchMovie(apiKey: String, title: String): MoviesResult {
        return try {
            val movies = apiService.searchMovie(apiKey, title)

            movieDao.insertMoviesList(movies.results.map { movie: Movie ->
//                val isFavorite = currentPlayingNowMoviesMap[movie.id]?.isFavorite ?: false
                MovieEntity(
                    id = movie.id,
                    title = movie.title,
                    type = "search",
                    releaseDate = movie.releaseDate,
                    posterPath = movie.posterPath,
                    backdropPath = movie.backdropPath,
                    voteAverage = movie.voteAverage,
                    overview = movie.overview,
                )
            })

            movies
        } catch (e: IOException) {
            Log.e("MoviesRepository", "Error fetching data from network")
            throw e
        } catch (e: Exception) {
            Log.e("MoviesRepository", "Unknown error", e)
            throw e
        }
    }

    suspend fun fetchPlayingNowMovies(apiKey: String) {
        try {
            val playingMovies = apiService.getNowPlayingMoviesList(apiKey)

            val currentPlayingNowMovies = movieDao.getPlayingNowMovies().first()
            val currentPlayingNowMoviesMap = currentPlayingNowMovies.associateBy { it.id }

            movieDao.insertMoviesList(playingMovies.results.map { movie: Movie ->
                val isFavorite = currentPlayingNowMoviesMap[movie.id]?.isFavorite ?: false
                MovieEntity(
                    id = movie.id,
                    title = movie.title,
                    type = "playingNow",
                    releaseDate = movie.releaseDate,
                    posterPath = movie.posterPath,
                    backdropPath = movie.backdropPath,
                    voteAverage = movie.voteAverage,
                    overview = movie.overview,
                    isFavorite = isFavorite
                )
            })
        } catch (e: IOException) {
            Log.e("MoviesRepository", "Error fetching data from network")
        } catch (e: Exception) {
            Log.e("MoviesRepository", "Unknown error", e)
        }
    }

    suspend fun fetchTvSeriesPopular(apiKey: String) {
        try {
            val tvSeriesPopularList = apiService.getTvSeriesPopularList(apiKey)

            val currentTvSeriesPopular = movieDao.getPopularTvSeries().first()
            val currentTvSeriesPopularMap = currentTvSeriesPopular.associateBy { it.id }

            movieDao.insertTvSeriesList(tvSeriesPopularList.results.map { tvSeries ->
                val isFavorite = currentTvSeriesPopularMap[tvSeries.id]?.isFavorite ?: false
                TvSeriesEntity(
                    id = tvSeries.id,
                    name = tvSeries.name,
                    type = "popular",
                    overview = tvSeries.overview,
                    posterPath = tvSeries.posterPath,
                    backdropPath = tvSeries.backdropPath,
                    firstAirDate = tvSeries.firstAirDate,
                    voteAverage = tvSeries.voteAverage,
                    isFavorite = isFavorite
                )
            })
        } catch (e: IOException) {
            Log.e("MoviesRepository", "Error fetching data from network")
        } catch (e: Exception) {
            Log.e("MoviesRepository", "Unknown error", e)
        }
    }

    suspend fun fetchUpcomingMovies(apiKey: String) {
        try {
            val upcomingMovieList = apiService.getUpcomingMoviesList(apiKey)

            val currentUpcomingMovies = movieDao.getUpcomingMovies().first()
            val currentUpcomingMoviesMap = currentUpcomingMovies.associateBy { it.id }

            movieDao.insertMoviesList(upcomingMovieList.results.map { movie: Movie ->
                val isFavorite = currentUpcomingMoviesMap[movie.id]?.isFavorite ?: false
                MovieEntity(
                    id = movie.id,
                    title = movie.title,
                    type = "upcoming",
                    releaseDate = movie.releaseDate,
                    posterPath = movie.posterPath,
                    backdropPath = movie.backdropPath,
                    voteAverage = movie.voteAverage,
                    overview = movie.overview,
                    isFavorite = isFavorite
                )
            })
        } catch (e: IOException) {
            Log.e("MoviesRepository", "Error fetching data from network")
        } catch (e: Exception) {
            Log.e("MoviesRepository", "Unknown error", e)
        }
    }

    suspend fun getAndInsertMovieCastById(apiKey: String, movieId: String) {
        try {
            val movieCast = apiService.getMovieCastById(apiKey = apiKey, id = movieId)

            movieDao.insertMovieCast(movieCast.cast.map { cast: Cast ->
                MovieCastEntity(
                    movieId = movieId.toInt(),
                    id = cast.id,
                    name = cast.name,
                    character = cast.character,
                    profilePath = cast.profilePath
                )
            })
        } catch (e: IOException) {
            Log.e("MoviesRepository", "Error fetching data from network")
        } catch (e: Exception) {
            Log.e("MoviesRepository", "Unknown error", e)
        }
    }
}