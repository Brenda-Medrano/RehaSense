package com.rehasense.app.domain.auth

/** Identidad entregada por Authentication; no representa un rol autorizado. */
data class AuthenticatedUser(
    val uid: String,
    val email: String?
)
