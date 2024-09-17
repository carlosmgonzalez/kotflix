package com.carlosmgonzalez.kotflix.ui.app.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.carlosmgonzalez.kotflix.ui.KotflixRoutes
import com.carlosmgonzalez.kotflix.ui.app.MoviesViewModel
import com.carlosmgonzalez.kotflix.ui.auth.AuthViewModel
import com.carlosmgonzalez.kotflix.ui.layout.LayoutNavbar

@Composable
fun FavoriteScreen(
    currentScreen: KotflixRoutes,
    navigateTo: (route: String) -> Unit,
    authViewModel: AuthViewModel,
    moviesViewModel: MoviesViewModel,
    navigateToMovieDetail: (movieId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val favoritesMovies by moviesViewModel.favoritesMoviesUiState.collectAsState()
    LayoutNavbar(
        currentScreen,
        navigateTo = navigateTo,
        authViewModel = authViewModel
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = modifier.padding(bottom = 10.dp, end = 10.dp)
        ) {
            items(
                items = favoritesMovies.movies,
                key = {movie -> movie.id}
            ) { movie ->
                CardMovie(
                    movie = movie,
                    navigateToMovieDetail = navigateToMovieDetail,
                    addMovieToFavorite = {
                        moviesViewModel.addMovieToFavorites(movie)
                    },
                    size = Size.Medium
                )
            }
        }
    }
}