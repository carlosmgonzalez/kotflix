package com.carlosmgonzalez.kotflix.ui.app.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.UnstableApi
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.carlosmgonzalez.kotflix.ui.KotflixRoutes
import com.carlosmgonzalez.kotflix.ui.auth.AuthViewModel
import com.carlosmgonzalez.kotflix.ui.layout.LayoutNavbar
import com.google.firebase.auth.FirebaseUser

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun ProfileScreen(
    currentScreen: KotflixRoutes,
    navigateTo: (route: String) -> Unit,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier,
) {
    val authUiState by authViewModel.uiState.collectAsState()
    val user = authUiState.user

    LayoutNavbar(
        currentScreen,
        navigateTo = navigateTo,
        authViewModel = authViewModel
    ) {
        Column(modifier) {
            ProfileCard(
                authViewModel = authViewModel,
                user = user
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCard(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    user: FirebaseUser?
) {
    var userPhoto by rememberSaveable { mutableStateOf(user?.photoUrl) }
    var displayName by rememberSaveable { mutableStateOf(user?.displayName) }

    var isEditingName by rememberSaveable { mutableStateOf(false) }

    val interactionSource = remember { MutableInteractionSource() }

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
                    modifier = Modifier
                        .size(100.dp)
                        .clip(shape = RoundedCornerShape(10)),
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
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    if(isEditingName) {
                        BasicTextField(
                            value = displayName ?: "",
                            onValueChange = {displayName = it},
                            maxLines = 1,
                            singleLine = true,
                            modifier = Modifier
                                .height(50.dp)
                                .fillMaxWidth(0.8f),
                            textStyle = TextStyle(
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                lineHeight = TextUnit(
                                    type = TextUnitType(1),
                                    value = 0.4F
                                )
                            ),
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.secondary),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    authViewModel.updateDisplayName(displayName) {displayName = it}
                                    isEditingName = false
                                }
                            )
                        ) { innerTextField ->
                            TextFieldDefaults.DecorationBox(
                                value = displayName ?: "",
                                innerTextField = innerTextField,
                                singleLine = true,
                                enabled = true,
                                interactionSource = interactionSource,
                                visualTransformation = VisualTransformation.None,
                                contentPadding = PaddingValues(0.dp),
                            )
                        }
                    } else {
                        Text(
                            text = if (displayName == null || displayName?.length == 0) "Unnamed"
                            else displayName ?: "Unnamed",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.fillMaxWidth(0.8f)
                        )
                    }

                    IconButton(
                        onClick = {isEditingName = !isEditingName},
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit user photo",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Text(
                    text = user?.email ?: "Without email",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}