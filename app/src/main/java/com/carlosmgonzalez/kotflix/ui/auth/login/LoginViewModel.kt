package com.carlosmgonzalez.kotflix.ui.auth.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel: ViewModel() {
    private val _iuState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _iuState.asStateFlow()

    private val auth = FirebaseAuth.getInstance()

    fun setEmail(email: String) {
        _iuState.update { currentState ->
            currentState.copy(
                email = email
            )
        }
    }

    fun setPassword(password: String) {
        _iuState.update { currentState ->
            currentState.copy(
                password = password
            )
        }
    }

    private fun setError(errorMessage: String) {
        _iuState.update { currentState ->
            currentState.copy(
                error = errorMessage
            )
        }
    }

    fun cleanError() {
        _iuState.update { currentState ->
            currentState.copy(
                error = null
            )
        }
    }

    fun onLogin() {
        val email = _iuState.value.email
        val password = _iuState.value.password

        val isValidEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isValidPassword = password.isNotEmpty()

        if (isValidEmail && isValidPassword) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Login", "SignInWithEmail:success")
                    } else {
                        Log.w("Login", "signInWithEmail:failure", task.exception)
                        setError(task.exception?.message ?: "Something went wrong")
                    }
                }
        }

        if (!isValidEmail) {
            setError("Email is required")
        } else if (!isValidPassword){
            setError("Password is invalid")
        }
    }
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val error: String? = null
)