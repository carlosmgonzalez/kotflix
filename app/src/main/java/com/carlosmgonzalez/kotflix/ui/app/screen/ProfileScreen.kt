package com.carlosmgonzalez.kotflix.ui.app.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.carlosmgonzalez.kotflix.ui.KotflixRoutes
import com.carlosmgonzalez.kotflix.ui.auth.AuthViewModel
import com.carlosmgonzalez.kotflix.ui.layout.LayoutNavbar

@Composable
fun ProfileScreen(
    currentScreen: KotflixRoutes,
    navigateTo: (route: String) -> Unit,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier,
) {
    val authUiState by authViewModel.uiState.collectAsState()
    val user = authUiState.user

    var userPhoto by remember { mutableStateOf(user?.photoUrl) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {uri ->
            if(uri != null) authViewModel.updateUserPhoto(uri) {userPhoto = it}
        }
    )

    fun launchPhotoPicker() {
        photoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    LayoutNavbar(
        currentScreen,
        navigateTo = navigateTo,
        authViewModel = authViewModel
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            Row(
                modifier = Modifier.padding(5.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(userPhoto ?: "https://res.cloudinary.com/difikt7so/image/upload/v1725832986/android-apps/pe0iael3vcfashyx50qr.jpg")
                            .build(),
                        contentDescription = null,
                        modifier = Modifier.size(100.dp).clip(shape = RoundedCornerShape(10)),
                        contentScale = ContentScale.Crop
                    )
                    IconButton(
                        onClick = {launchPhotoPicker()},
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(30.dp)
                            .offset(x = (-3).dp, y = (-3).dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit user photo",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Column{
                    Text(
                        text = user?.displayName ?: "Unnamed",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = user?.email ?: "Without email",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}