package com.rehasense.app.presentation.auth

/** Validación de formularios sin dependencias Android ni de Firebase. */
internal object AuthFormValidator {
    private val emailPattern = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")

    fun email(value: String): String? {
        val normalized = value.trim()
        if (normalized.isEmpty()) return "Introduce tu correo electrónico."
        if (normalized.length > 254 || !emailPattern.matches(normalized)) {
            return "Introduce un correo válido."
        }
        return null
    }

    fun signIn(value: String, password: String): String? =
        email(value) ?: if (password.isEmpty()) "Introduce tu contraseña." else null

    fun register(value: String, password: String, confirmation: String): String? =
        email(value)
            ?: when {
                password.length < 6 -> "La contraseña debe tener al menos 6 caracteres."
                password != confirmation -> "Las contraseñas no coinciden."
                else -> null
            }
}
