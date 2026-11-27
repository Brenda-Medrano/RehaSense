package com.rehasense.app.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

private val DarkBlue = Color(0xFF153A5A)
private val PaleBlue = Color(0xFFEAF6FC)

@Composable
fun AuthScreen(viewModel: AuthViewModel) {
    val ui by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp, vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().widthIn(max = 480.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("RehaSense", color = DarkBlue, style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold)
            Text("Rehabilitación y seguimiento", color = DarkBlue,
                style = MaterialTheme.typography.bodyMedium)

            if (!ui.isSessionReady) {
                CircularProgressIndicator()
                Text("Comprobando tu sesión…")
            } else if (ui.currentUser != null) {
                SessionContent(ui, onSignOut = viewModel::signOut)
            } else {
                AuthenticationForm(ui, viewModel)
            }
        }
    }
}

@Composable
private fun SessionContent(ui: AuthUiState, onSignOut: () -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = PaleBlue)) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Sesión iniciada", fontWeight = FontWeight.Bold, color = DarkBlue,
                style = MaterialTheme.typography.titleLarge)
            Text(ui.currentUser?.email ?: "Correo no disponible")
            Text("Iniciaste sesión con Firebase Authentication. Tu correo podría no estar verificado y todavía no tienes un perfil clínico autorizado. No se habilita el acceso a pacientes en este parche.")
            if (ui.notice != null) Text(ui.notice, color = DarkBlue)
            if (ui.error != null) Text(ui.error, color = MaterialTheme.colorScheme.error)
            Button(onClick = onSignOut, enabled = !ui.isBusy, modifier = Modifier.fillMaxWidth()) {
                Text("Cerrar sesión")
            }
        }
    }
}

@Composable
private fun AuthenticationForm(ui: AuthUiState, viewModel: AuthViewModel) {
    var email by rememberSaveable { mutableStateOf("") }
    // Las contraseñas permanecen únicamente en memoria de la composición.
    var password by remember { mutableStateOf("") }
    var confirmation by remember { mutableStateOf("") }

    val title = when (ui.page) {
        AuthPage.SIGN_IN -> "Iniciar sesión"
        AuthPage.REGISTER -> "Crear cuenta"
        AuthPage.RESET_PASSWORD -> "Recuperar contraseña"
    }

    Card(colors = CardDefaults.cardColors(containerColor = PaleBlue)) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(title, color = DarkBlue, style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold)

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                enabled = !ui.isBusy,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            if (ui.page != AuthPage.RESET_PASSWORD) {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    enabled = !ui.isBusy,
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (ui.page == AuthPage.REGISTER) {
                OutlinedTextField(
                    value = confirmation,
                    onValueChange = { confirmation = it },
                    label = { Text("Confirmar contraseña") },
                    enabled = !ui.isBusy,
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (ui.error != null) Text(ui.error, color = MaterialTheme.colorScheme.error)
            if (ui.notice != null) Text(ui.notice, color = DarkBlue)

            Button(
                enabled = !ui.isBusy,
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    when (ui.page) {
                        AuthPage.SIGN_IN -> viewModel.signIn(email, password)
                        AuthPage.REGISTER -> viewModel.register(email, password, confirmation)
                        AuthPage.RESET_PASSWORD -> viewModel.resetPassword(email)
                    }
                }
            ) {
                if (ui.isBusy) CircularProgressIndicator(modifier = Modifier.height(18.dp),
                    strokeWidth = 2.dp)
                else Text(title)
            }

            when (ui.page) {
                AuthPage.SIGN_IN -> {
                    TextButton(onClick = {
                        password = ""; confirmation = ""; viewModel.openPage(AuthPage.REGISTER)
                    }, enabled = !ui.isBusy) { Text("¿No tienes cuenta? Regístrate") }
                    TextButton(onClick = {
                        password = ""; viewModel.openPage(AuthPage.RESET_PASSWORD)
                    }, enabled = !ui.isBusy) { Text("¿Olvidaste tu contraseña?") }
                }
                AuthPage.REGISTER, AuthPage.RESET_PASSWORD -> {
                    TextButton(onClick = {
                        password = ""; confirmation = ""; viewModel.openPage(AuthPage.SIGN_IN)
                    }, enabled = !ui.isBusy) { Text("Volver a iniciar sesión") }
                }
            }
        }
    }
    Spacer(Modifier.height(4.dp))
    Text("Los perfiles profesionales y los datos clínicos se habilitarán en parches posteriores.",
        style = MaterialTheme.typography.bodySmall, color = DarkBlue)
}
