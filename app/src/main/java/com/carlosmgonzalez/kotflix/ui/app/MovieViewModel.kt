package com.carlosmgonzalez.kotflix.ui.app

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.carlosmgonzalez.kotflix.KotflixApplication
import com.carlosmgonzalez.kotflix.data.MovieCastEntity
import com.carlosmgonzalez.kotflix.data.MovieEntity
import com.carlosmgonzalez.kotflix.data.MoviesRepository
import com.carlosmgonzalez.kotflix.data.TvSeriesEntity
import com.carlosmgonzalez.kotflix.model.Movie
import com.carlosmgonzalez.kotflix.BuildConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

const val apiKey = BuildConfig.API_KEY

data class UpcomingMoviesUiState(
    val movies: List<MovieEntity> = listOf()
)

data class PlayingNowMovieUiState(
    val movies: List<MovieEntity> = listOf(),
)

data class TvSeriesUiState(
    val tvSeries: List<TvSeriesEntity> = listOf()
)

data class FavoritesMoviesUiState(
    val movies: List<MovieEntity> = listOf()
)

sealed interface SearchMovieUiState {
    data class Success(val movies: List<Movie>): SearchMovieUiState
    data object Loading: SearchMovieUiState
    data object Error: SearchMovieUiState
}

class MoviesViewModel(
    private val moviesRepository: MoviesRepository
) : ViewModel() {
    val playingNowMovieUiState: StateFlow<PlayingNowMovieUiState> =
        moviesRepository.playingNowMovies.map { PlayingNowMovieUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = PlayingNowMovieUiState()
            )

    val upcomingMoviesUiState: StateFlow<UpcomingMoviesUiState> =
        moviesRepository.upcomingMovies.map { UpcomingMoviesUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = UpcomingMoviesUiState()
            )

    val favoritesMoviesUiState: StateFlow<FavoritesMoviesUiState> =
        moviesRepository.favoritesMovies.map { FavoritesMoviesUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = FavoritesMoviesUiState()
            )

    val tvSeriesDbUiState: StateFlow<TvSeriesUiState> =
        moviesRepository.tvSeriesPopular.map { TvSeriesUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = TvSeriesUiState()
            )

    var searchMovieUiState: SearchMovieUiState by mutableStateOf(SearchMovieUiState.Loading)
        private set

    init {
        fetchMoviesPlayingNowMovies()
        fetchTvSeriesPopular()
        fetchUpcomingMovies()
    }

    fun searchMovie(title: String) {
        viewModelScope.launch {
            searchMovieUiState = try {
                SearchMovieUiState.Loading
                val movies = moviesRepository.fetchSearchMovie(apiKey, title).results
                SearchMovieUiState.Success(movies = movies)
            } catch (e: Exception) {
                Log.e("searchMovie", "Error search movie", e)
                SearchMovieUiState.Error
            }
        }
    }

    fun addMovieToFavorites(movie: MovieEntity) {
        val movieFavorite = movie.copy(
            isFavorite = !movie.isFavorite
        )
        viewModelScope.launch {
            moviesRepository.addMovieToFavorites(movieFavorite)
        }
    }

    private fun fetchMoviesPlayingNowMovies() {
        viewModelScope.launch {
            moviesRepository.fetchPlayingNowMovies(apiKey)
        }
    }

    private fun fetchTvSeriesPopular() {
        viewModelScope.launch {
            moviesRepository.fetchTvSeriesPopular(apiKey)
        }
    }

    private fun fetchUpcomingMovies() {
        viewModelScope.launch {
            moviesRepository.fetchUpcomingMovies(apiKey)
        }
    }

    fun getMovieDetailById(id: String): Flow<MovieEntity> = moviesRepository.getMovieDetailById(id)

    fun getMovieCastById(id: String): Flow<List<MovieCastEntity>> = moviesRepository.getMovieCastById(id)

    fun getAndInsertMovieCastById(movieId: String) {
        viewModelScope.launch {
            moviesRepository.getAndInsertMovieCastById(apiKey = apiKey, movieId = movieId)
        }
    }

    companion object {
        val factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as KotflixApplication)
                MoviesViewModel(
                    moviesRepository = application.container.moviesRepository
                )
            }
        }
    }
}

//    .stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5_000L),
//        initialValue = MoviesDbUiState()
//    )

//sealed interface MoviesUiState {
//    data class Success(
//        val playingMoviesList: MoviesResult,
//        val tvSeriesAiringTodayList: TvSeriesResult,
//        val tvSeriesPopularList: TvSeriesResult
//    ) : MoviesUiState
//
//    data object Loading : MoviesUiState
//    data object Error : MoviesUiState
//}

//sealed interface MovieDetailUiState {
//    data class Success(
//        val movieDetail: Movie,
//        val castMovie: CreditsResult
//    ) : MovieDetailUiState
//
//    data object Loading : MovieDetailUiState
//    data object Error : MovieDetailUiState
//}

//    var moviesUiState: MoviesUiState by mutableStateOf(MoviesUiState.Loading)
//        private set
//
//    var movieDetailUiState: MovieDetailUiState by mutableStateOf(MovieDetailUiState.Loading)
//        private set

//    private fun getNowPlayingMoviesList() {
//        viewModelScope.launch {
//            moviesUiState = MoviesUiState.Loading
//            moviesUiState = try {
//                val playingMoviesList = KotflixApi.retrofitService.getNowPlayingMoviesList(apiKey)
//                val tvSeriesAiringTodayList = KotflixApi.retrofitService.getTvSeriesAiringTodayList(
//                    apiKey
//                )
//                val tvSeriesPopularList = KotflixApi.retrofitService.getTvSeriesPopularList(apiKey)
//
//                val playingMoviesListDb = movieDao.getAllMovies()
//
//                MoviesUiState.Success(
//                    playingMoviesList,
//                    tvSeriesAiringTodayList,
//                    tvSeriesPopularList
//                )
//            } catch (e: Exception) {
//                Log.e("errorGetPlayingNowMovies", e.message ?: "Error getting movies list")
//                MoviesUiState.Error
//            }
//        }
//    }
//
//    fun getMovieDetailById(movieId: String) {
//        viewModelScope.launch {
//            movieDetailUiState = MovieDetailUiState.Loading
//            movieDetailUiState = try {
//                val movieDetail = KotflixApi.retrofitService.getMovieDetailById(movieId, apiKey)
//                val castMovie = KotflixApi.retrofitService.getCastMovieById(movieId, apiKey)
//                MovieDetailUiState.Success(movieDetail, castMovie)
//            } catch (e: Exception) {
//                Log.e("errorGetMovieDetailById", e.message ?: "Error getting movie detail")
//                MovieDetailUiState.Error
//            }
//        }
//    }