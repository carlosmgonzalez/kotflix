package com.carlosmgonzalez.kotflix.ui.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.carlosmgonzalez.kotflix.ui.KotflixRoutes
import com.carlosmgonzalez.kotflix.ui.auth.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayoutNavbar(
    currentScreen: KotflixRoutes,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    navigateTo: (route: String) -> Unit = {},
    content: @Composable () -> Unit
) {
    val navigationListContent = listOf(
        NavigationItemContent(
            navigationRoute = KotflixRoutes.Home,
            icon = Icons.Default.Home,
            text = "Home"
        ),
        NavigationItemContent(
            navigationRoute = KotflixRoutes.Favorites,
            icon = Icons.Default.Favorite,
            text = "Favorites"
        ),
        NavigationItemContent(
            navigationRoute = KotflixRoutes.Profile,
            icon = Icons.Default.Person,
            text = "Profile"
        )
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = currentScreen.title)
                },
                actions = {
                    if (currentScreen == KotflixRoutes.Profile) {
                        Button(
                            onClick = { authViewModel.sighOut()},
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(text = "Sign out")
                        }
                    } else if (currentScreen == KotflixRoutes.Home) {
                        IconButton(
                            onClick = {
                                navigateTo(KotflixRoutes.Search.name)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Search"
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                navigationListContent.forEach {
                    NavigationBarItem(
                        selected = currentScreen == it.navigationRoute,
                        onClick = { navigateTo(it.navigationRoute.name) },
                        icon = {
                            Icon(imageVector = it.icon, contentDescription = null)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier
                .fillMaxSize()
                .padding(innerPadding)
//                .padding(horizontal = 10.dp)
        ) {
            content()
        }
    }
}

private data class NavigationItemContent(
    val navigationRoute: KotflixRoutes,
    val icon: ImageVector,
    val text: String
)
