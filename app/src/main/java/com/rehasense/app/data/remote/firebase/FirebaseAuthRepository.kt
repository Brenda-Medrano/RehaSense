package com.rehasense.app.data.remote.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.rehasense.app.domain.auth.AuthenticatedUser
import com.rehasense.app.domain.repository.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

/** Ninguna operación de Authentication concede permisos sobre datos médicos. */
class FirebaseAuthRepository(
    private val auth: FirebaseAuth
) : AuthRepository {

    override val session: Flow<AuthenticatedUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser?.toDomain())
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }.distinctUntilChanged()

    override suspend fun register(email: String, password: String): AuthenticatedUser {
        val normalizedEmail = email.trim()
        require(normalizedEmail.isNotEmpty()) { "El correo es obligatorio" }
        require(password.isNotEmpty()) { "La contraseña es obligatoria" }

        val result = auth.createUserWithEmailAndPassword(normalizedEmail, password)
            .awaitFirebaseTask()
        return requireNotNull(result.user) { "Firebase no devolvió un usuario" }.toDomain()
    }

    override suspend fun signIn(email: String, password: String): AuthenticatedUser {
        val normalizedEmail = email.trim()
        require(normalizedEmail.isNotEmpty()) { "El correo es obligatorio" }
        require(password.isNotEmpty()) { "La contraseña es obligatoria" }

        val result = auth.signInWithEmailAndPassword(normalizedEmail, password)
            .awaitFirebaseTask()
        return requireNotNull(result.user) { "Firebase no devolvió un usuario" }.toDomain()
    }

    override suspend fun sendPasswordReset(email: String) {
        val normalizedEmail = email.trim()
        require(normalizedEmail.isNotEmpty()) { "El correo es obligatorio" }
        auth.sendPasswordResetEmail(normalizedEmail).awaitFirebaseTask()
    }

    override fun signOut() = auth.signOut()

    private fun FirebaseUser.toDomain() = AuthenticatedUser(uid = uid, email = email)
}
