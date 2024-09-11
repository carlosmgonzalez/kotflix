package com.carlosmgonzalez.kotflix.ui.layout

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import com.carlosmgonzalez.kotflix.ui.KotflixRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayoutAuth(
    navigateBack: () -> Unit,
    previousBackStackEntry: NavBackStackEntry?,
    currentScreen: KotflixRoutes,
    content: @Composable (innerPadding: PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = currentScreen.title)
                },
                navigationIcon = {
                    if (previousBackStackEntry != null) {
                        IconButton(onClick = { navigateBack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                }
            )
        },
        modifier = Modifier.padding(horizontal = 16.dp)
    ) { innerPadding ->
        content(innerPadding)
    }
}