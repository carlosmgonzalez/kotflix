package com.carlosmgonzalez.kotflix.ui.auth.register

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RegisterViewModel : ViewModel() {
    private val _iuState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _iuState.asStateFlow()

    val auth = FirebaseAuth.getInstance()

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

    fun setRepeatPassword(password: String) {
        _iuState.update { currentState ->
            currentState.copy(
                repeatPassword = password
            )
        }
    }

    private fun setError(messageError: String) {
        _iuState.update { currentState ->
            currentState.copy(
                error = messageError
            )
        }
    }

    private fun reset() {
        _iuState.update {
            RegisterUiState(
                email = "",
                password = "",
                repeatPassword = "",
                error = null
            )
        }
    }

    fun onRegister() {
        val email = _iuState.value.email
        val password = _iuState.value.password
        val repeatPassword = _iuState.value.repeatPassword

        val isValidEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isValidPassword =
            password == repeatPassword && password.isNotEmpty() && repeatPassword.isNotEmpty()

        if (isValidPassword && isValidEmail) {
            Log.i("Password", "$password and $repeatPassword")
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Register", "createUserWithEmail:success")
                        reset()
                    } else {
                        Log.w("Register", "createUserWithEmail:failure", task.exception)
                        setError(task.exception?.message ?: "Something went wrong")
                    }
                }
        }

        if(!isValidEmail) {
            setError("Email invalid")
        } else if(!isValidPassword) {
            setError("Password invalid")
        }
    }

    fun cleanError() {
        _iuState.update { currentState ->
            currentState.copy(
                error = null
            )
        }
    }
}

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val error: String? = null
)