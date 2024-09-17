package com.carlosmgonzalez.kotflix.ui.app.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.carlosmgonzalez.kotflix.R
import com.carlosmgonzalez.kotflix.data.MovieCastEntity
import com.carlosmgonzalez.kotflix.data.MovieEntity
import com.carlosmgonzalez.kotflix.ui.app.MoviesViewModel
import com.carlosmgonzalez.kotflix.utils.truncate

@Composable
fun MovieDetailScreen(
    movieId: String,
    moviesViewModel: MoviesViewModel,
    navigateBack: () -> Unit
) {
    LaunchedEffect(movieId) {
        moviesViewModel.getAndInsertMovieCastById(movieId)
    }

    val initialMovieDetail =  MovieEntity(
        id = 0,
        title = "",
        type = "playingNow",
        releaseDate = "",
        voteAverage = 0f,
        overview = ""
    )

    val movieDetail by moviesViewModel.getMovieDetailById(movieId).collectAsState(initialMovieDetail)
    val movieCastList by moviesViewModel.getMovieCastById(movieId).collectAsState(listOf())

    MovieDetail(
        movie = movieDetail,
        navigateBack = navigateBack,
        listCast = movieCastList,
        onFavoriteMovie = {
            moviesViewModel.addMovieToFavorites(it)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetail(
    movie: MovieEntity,
    listCast: List<MovieCastEntity>,
    navigateBack: () -> Unit,
    onFavoriteMovie: (movie: MovieEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(movie.title)
                },
                navigationIcon = {
                    IconButton(onClick = { navigateBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (movie.backdropPath != null) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data("https://image.tmdb.org/t/p/w500/${movie.backdropPath}")
                                .build(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.FillWidth,
                            placeholder = painterResource(R.drawable.placeholder_image_large)
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.placeholder_image_large),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.FillWidth,
                        )
                    }
                }
                Column(
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        AssistChip(
                            onClick = {},
                            label = {
                                Text(
                                    text = movie.releaseDate,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.DateRange,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        )
                        AssistChip(
                            onClick = {},
                            label = {
                                Text(
                                    text = "${movie.voteAverage}",
                                    style = MaterialTheme.typography.labelLarge
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.ThumbUp,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        )
                        AssistChip(
                            onClick = {
                                onFavoriteMovie(movie)
                            },
                            label = {
                                Text(
                                    text = "Favorite",
                                    style = MaterialTheme.typography.labelLarge
                                )
                            },
                            leadingIcon = {
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
                        )
                    }
                    Text(text = "Override:", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(2.dp))
                    Text(text = movie.overview, style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(6.dp))
                    Text(text = "Cast:", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(2.dp))
                    CastList(listCast = listCast)
                }
            }
        }
    }
}

@Composable
fun CastList(
    listCast: List<MovieCastEntity>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(
            items = listCast,
            key = { cast -> cast.id }
        ) { cast ->
            CastCard(cast)
        }
    }
}

@Composable
fun CastCard(
    cast: MovieCastEntity,
    modifier: Modifier = Modifier
) {
    Card {
        Card(
            modifier.size(width = 180.dp, height = 270.dp)
        ) {
            if (cast.profilePath != null ) {
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data("https://image.tmdb.org/t/p/w500/${cast.profilePath}")
                        .build(),
                    contentDescription = "character",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize(),
                    placeholder = painterResource(R.drawable.no_photo_cast)
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.no_photo_cast),
                    contentDescription = "character",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
        Text(text = cast.name.truncate(), modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp))
    }
}
