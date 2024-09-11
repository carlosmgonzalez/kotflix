package com.carlosmgonzalez.kotflix.ui.auth.register

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import com.carlosmgonzalez.kotflix.ui.KotflixRoutes
import com.carlosmgonzalez.kotflix.ui.layout.LayoutAuth

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    previousBackStackEntry: NavBackStackEntry?,
    currentScreen: KotflixRoutes,
    viewModel: RegisterViewModel = viewModel()
) {
    val registerUiState by viewModel.uiState.collectAsState()
    LayoutAuth(
        navigateBack = navigateBack,
        previousBackStackEntry = previousBackStackEntry,
        currentScreen  = currentScreen
    ) { innerPadding ->
        Box(modifier.fillMaxSize().padding(innerPadding)) {
            Column(
                Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Kotflix", style = MaterialTheme.typography.displayLarge, fontSize = 80.sp)
                TextField(
                    value = registerUiState.email,
                    onValueChange = {viewModel.setEmail(it)},
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(text = "Email")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    singleLine = true,
                    maxLines = 1,
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = registerUiState.password,
                    onValueChange = {viewModel.setPassword(it)},
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(text = "Password")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next,
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    maxLines = 1,
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = registerUiState.repeatPassword,
                    onValueChange = {viewModel.setRepeatPassword(it)},
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(text = "Repeat password")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    maxLines = 1,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = { viewModel.onRegister() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Register", style = MaterialTheme.typography.titleMedium)
                }
            }
            if (registerUiState.error != null) {
                Toast.makeText(
                    LocalContext.current,
                    "${registerUiState.error}",
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.cleanError()
            }
        }
    }

}