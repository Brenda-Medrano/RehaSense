package com.rehasense.app.domain.repository

import com.rehasense.app.domain.auth.AuthenticatedUser
import kotlinx.coroutines.flow.Flow

/** Contrato independiente del SDK de Firebase. */
interface AuthRepository {
    /** null representa una sesión cerrada. */
    val session: Flow<AuthenticatedUser?>

    suspend fun register(email: String, password: String): AuthenticatedUser
    suspend fun signIn(email: String, password: String): AuthenticatedUser
    suspend fun sendPasswordReset(email: String)
    fun signOut()
}
