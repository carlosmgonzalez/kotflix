package com.carlosmgonzalez.kotflix.ui.app.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.carlosmgonzalez.kotflix.data.MovieEntity
import com.carlosmgonzalez.kotflix.model.toMovieEntity
import com.carlosmgonzalez.kotflix.ui.app.MoviesViewModel
import com.carlosmgonzalez.kotflix.ui.app.SearchMovieUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navigateBack: () -> Unit,
    navigateToMovieDetail: (movieId: String) -> Unit,
    moviesViewModel: MoviesViewModel,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search") },
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
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            var movieTitle by rememberSaveable { mutableStateOf("") }
            val searchMovieUiState = moviesViewModel.searchMovieUiState

            Column {
                TextField(
                    value = movieTitle,
                    onValueChange = {movieTitle = it},
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                    singleLine = true,
                    maxLines = 1,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search"
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            moviesViewModel.searchMovie(movieTitle)
                        }
                    )
                )
                Spacer(Modifier.height(10.dp))

                when(searchMovieUiState) {
                    is SearchMovieUiState.Success -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = modifier.padding(bottom = 10.dp).padding(end = 10.dp)
                        ) {
                            items(
                                items = searchMovieUiState.movies,
                                key = {movie -> movie.id}
                            ) { movie ->
                                val initialMovieDetail =  MovieEntity(
                                    id = 0,
                                    title = "",
                                    type = "playingNow",
                                    releaseDate = "",
                                    voteAverage = 0f,
                                    overview = ""
                                )
                                val movieEntity = movie.toMovieEntity(
                                    type = "search",
                                    isFavorite = moviesViewModel.getMovieDetailById(movie.id.toString()).collectAsState(initialMovieDetail).value.isFavorite
                                )
                                CardMovie(
                                    movie = movieEntity,
                                    navigateToMovieDetail = navigateToMovieDetail,
                                    addMovieToFavorite = {
                                        moviesViewModel.addMovieToFavorites(movieEntity)
                                    },
                                    size = true
                                )
                            }
                        }
                    }
                    else -> {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text("Search a movie", style = MaterialTheme.typography.titleLarge)
                            Text("or maybe is a Error or Loading", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }
        }
    }
}