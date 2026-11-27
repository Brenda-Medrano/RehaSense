package com.rehasense.app.presentation.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rehasense.app.data.remote.firebase.FirebaseModule
import com.rehasense.app.domain.repository.AuthRepository

/** Entrada de autenticación de la Etapa 2; el flujo Room de la Etapa 1 no se elimina. */
class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = FirebaseModule.fromDefaultApp().authRepository
        val factory = AuthViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]

        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF168BC4),
                    onPrimary = Color.White,
                    secondary = Color(0xFF153A5A),
                    background = Color.White
                )
            ) {
                AuthScreen(viewModel)
            }
        }
    }
}

private class AuthViewModelFactory(
    private val repository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            "ViewModel desconocido: ${modelClass.name}"
        }
        @Suppress("UNCHECKED_CAST")
        return AuthViewModel(repository) as T
    }
}
