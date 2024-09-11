package com.carlosmgonzalez.kotflix.ui.auth.login

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
fun LoginScreen(
    modifier: Modifier = Modifier,
    navigateToRegister: () -> Unit,
    navigateBack: () -> Unit,
    previousBackStackEntry: NavBackStackEntry?,
    currentScreen: KotflixRoutes,
    viewModel: LoginViewModel = viewModel()
) {
    val loginUiState by viewModel.uiState.collectAsState()
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
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = loginUiState.email,
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
                    value = loginUiState.password,
                    onValueChange = {viewModel.setPassword(it)},
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(text = "Password")
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
                    onClick = { viewModel.onLogin() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Login", style = MaterialTheme.typography.titleMedium)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(0.dp)
                ) {
                    Text(text = "You have an account?", style = MaterialTheme.typography.labelMedium)
                    TextButton(
                        onClick = { navigateToRegister() },
                        contentPadding = PaddingValues(horizontal = 5.dp, vertical = 0.dp)
                    ) {
                        Text(text = "Click here", style = MaterialTheme.typography.labelLarge)
                    }
                }
                if (loginUiState.error != null) {
                    Toast.makeText(
                        LocalContext.current,
                        "${loginUiState.error}",
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.cleanError()
                }
            }
        }
    }

}