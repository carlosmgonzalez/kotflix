package com.carlosmgonzalez.kotflix.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.carlosmgonzalez.kotflix.ui.app.screen.FavoriteScreen
import com.carlosmgonzalez.kotflix.ui.auth.AuthViewModel
import com.carlosmgonzalez.kotflix.ui.auth.login.LoginScreen
import com.carlosmgonzalez.kotflix.ui.auth.register.RegisterScreen
import com.carlosmgonzalez.kotflix.ui.app.screen.HomeScreen
import com.carlosmgonzalez.kotflix.ui.app.MoviesViewModel
import com.carlosmgonzalez.kotflix.ui.app.screen.MovieDetailScreen
import com.carlosmgonzalez.kotflix.ui.app.screen.ProfileScreen
import com.carlosmgonzalez.kotflix.ui.app.screen.SearchScreen

enum class KotflixRoutes(val title: String){
    Login(title = "Login"),
    Register(title = "Register"),
    Home(title = "Kotflix"),
    Profile(title = "Profile"),
    Favorites(title = "Favorites"),
    Details(title = "Details"),
    Search(title = "Search")
}

@Composable
fun KotflixApp(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel(),
    moviesViewModel: MoviesViewModel = viewModel(factory = MoviesViewModel.factory)
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = KotflixRoutes.entries.toTypedArray().find { route ->
        backStackEntry?.destination?.route?.substringBefore("/") == route.name
    } ?: KotflixRoutes.Home

//    valueOf(
//        backStackEntry?.destination?.route ?: KotflixRoutes.Home.name
//    )

    val authUiState by authViewModel.uiState.collectAsState()
    val startDestination = if (authUiState.user != null) KotflixRoutes.Home else KotflixRoutes.Login

    fun navigationTo(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination.name,
        modifier = modifier
    ) {
        composable(route = KotflixRoutes.Login.name) {
            LoginScreen(
                navigateToRegister = {
                    navController.navigate(KotflixRoutes.Register.name)
                },
                navigateBack = { navController.navigateUp() },
                previousBackStackEntry = navController.previousBackStackEntry,
                currentScreen = currentScreen
            )
        }
        composable(route = KotflixRoutes.Register.name) {
            RegisterScreen(
                navigateBack = { navController.navigateUp() },
                previousBackStackEntry = navController.previousBackStackEntry,
                currentScreen = currentScreen
            )
        }
        composable(route = KotflixRoutes.Home.name) {
            HomeScreen(
                currentScreen = currentScreen,
                navigateTo = { navigationTo(it)},
                navigateToMovieDetail = {
                    navController.navigate("${KotflixRoutes.Details.name}/$it")
                },
                modifier = Modifier.fillMaxSize(),
                authViewModel = authViewModel,
                moviesViewModel = moviesViewModel
            )
        }
        composable(route = KotflixRoutes.Favorites.name) {
            FavoriteScreen(
                currentScreen = currentScreen,
                navigateTo = { navigationTo(it)},
                modifier = Modifier.fillMaxSize(),
                authViewModel = authViewModel,
                moviesViewModel = moviesViewModel,
                navigateToMovieDetail = {
                    navController.navigate("${KotflixRoutes.Details.name}/$it")
                }
            )
        }
        composable(route = KotflixRoutes.Profile.name) {
            ProfileScreen(
                currentScreen = currentScreen,
                navigateTo = { navigationTo(it) },
                authViewModel = authViewModel,
            )
        }
        composable(
            route = "${KotflixRoutes.Details.name}/{movieId}",
            arguments = listOf(navArgument("movieId") {type = NavType.StringType})
        ) { backStackEntry ->
            MovieDetailScreen(
                movieId = backStackEntry.arguments?.getString("movieId") ?: "",
                moviesViewModel = moviesViewModel,
                navigateBack = {navController.navigateUp()}
            )
        }
        composable(
            route = KotflixRoutes.Search.name
        ) {
            SearchScreen(
                navigateBack = {navController.navigateUp()},
                moviesViewModel = moviesViewModel,
                navigateToMovieDetail = {
                    navController.navigate("${KotflixRoutes.Details.name}/$it")
                }
            )
        }
    }
}