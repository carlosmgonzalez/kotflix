package com.carlosmgonzalez.kotflix.ui.app.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.carlosmgonzalez.kotflix.ui.auth.AuthViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.carlosmgonzalez.kotflix.R
import com.carlosmgonzalez.kotflix.data.MovieEntity
import com.carlosmgonzalez.kotflix.data.TvSeriesEntity
import com.carlosmgonzalez.kotflix.ui.KotflixRoutes
import com.carlosmgonzalez.kotflix.ui.app.MoviesViewModel
import com.carlosmgonzalez.kotflix.ui.layout.LayoutNavbar

@Composable
fun HomeScreen(
    currentScreen: KotflixRoutes,
    navigateTo: (route: String) -> Unit,
    navigateToMovieDetail: (movieId: String) -> Unit,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    moviesViewModel: MoviesViewModel
) {
    val upcomingMovies by moviesViewModel.upcomingMoviesUiState.collectAsState()
    val playingNowMovies by moviesViewModel.playingNowMovieUiState.collectAsState()
    val tvSeriesDbUiState by moviesViewModel.tvSeriesDbUiState.collectAsState()

    LayoutNavbar(
        currentScreen,
        navigateTo = navigateTo,
        authViewModel = authViewModel
    ) {
        Column(
            modifier
                .verticalScroll(rememberScrollState())
                .padding(bottom = 10.dp)
        ) {
            ListMovies(
                movies = playingNowMovies.movies,
                navigateToMovieDetail = navigateToMovieDetail,
                size = true,
                addMovieToFavorite = { moviesViewModel.addMovieToFavorites(it) },
                modifier = modifier
            )
            Spacer(Modifier.height(20.dp))
            ListMovies(
                movies = upcomingMovies.movies,
                navigateToMovieDetail = navigateToMovieDetail,
                size = false,
                title = "Upcoming movies",
                addMovieToFavorite = { moviesViewModel.addMovieToFavorites(it) },
                modifier = modifier
            )
            Spacer(Modifier.height(20.dp))
            ListTvSeries(
                tvSeriesList = tvSeriesDbUiState.tvSeries,
                title = "Popular tv series"
            )
        }
    }
}

@Composable
fun ListMovies(
    movies: List<MovieEntity>,
    navigateToMovieDetail: (movieId: String) -> Unit,
    size: Boolean,
    addMovieToFavorite: (movie: MovieEntity) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null
) {
    Column(modifier) {
        if (title != null) {
            Text(
                text = title,
                modifier = Modifier.padding(start = 10.dp),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(5.dp))
        }
        LazyRow {
            items(
                items = movies,
                key = { movie -> movie.id }
            ) { movie ->
                CardMovie(
                    movie = movie,
                    navigateToMovieDetail = navigateToMovieDetail,
                    size = size,
                    addMovieToFavorite = addMovieToFavorite
                )
            }
        }
    }
}

@Composable
fun CardMovie(
    movie: MovieEntity,
    modifier: Modifier = Modifier,
    navigateToMovieDetail: (movieId: String) -> Unit,
    addMovieToFavorite: (movie: MovieEntity) -> Unit,
    size: Boolean
) {
    Card(
        modifier = modifier
            .clickable { navigateToMovieDetail(movie.id.toString()) }
            .padding(start = 10.dp)
    ) {
        Box(
            modifier = if (size) Modifier.size(width = 200.dp, height = 300.dp)
            else Modifier.size(width = 100.dp, height = 150.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data("https://image.tmdb.org/t/p/w500/${movie.posterPath}")
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize(),
                placeholder = painterResource(R.drawable.placeholder_image)
            )
            IconButton(
                onClick = {
                    addMovieToFavorite(movie)
                },
                modifier = if (size) {
                    Modifier.align(Alignment.BottomEnd)
                } else {
                    Modifier
                        .padding(end = 5.dp, bottom = 5.dp)
                        .align(Alignment.BottomEnd)
                        .size(20.dp)
                }
            ) {
                if (movie.isFavorite) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color(0xFFC71F1F)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = Color(0xFF868686)
                    )
                }
            }
        }
    }

}

@Composable
fun ListTvSeries(
    tvSeriesList: List<TvSeriesEntity>,
    title: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            modifier = Modifier.padding(start = 10.dp),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(5.dp))
        LazyRow {
            items(
                items = tvSeriesList,
                key = { movie -> movie.id }
            ) { tvSeries ->
                CardTvSeries(
                    tvSeries = tvSeries
                )
            }
        }
    }
}

@Composable
fun CardTvSeries(
    tvSeries: TvSeriesEntity,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(start = 10.dp)
            .size(width = 100.dp, height = 150.dp)
    ) {
        Box {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data("https://image.tmdb.org/t/p/w500/${tvSeries.posterPath}")
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
            IconButton(
                onClick = {},
                modifier =
                Modifier
                    .padding(end = 5.dp, bottom = 5.dp)
                    .align(Alignment.BottomEnd)
                    .size(20.dp)

            ) {
                if (tvSeries.isFavorite) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color(0xFFC71F1F)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = Color(0xFF868686)
                    )
                }
            }
        }
    }
}
