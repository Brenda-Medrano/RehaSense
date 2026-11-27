package com.rehasense.app.data.remote.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rehasense.app.domain.model.CloudUserSnapshot
import com.rehasense.app.domain.repository.CloudUserRepository

/** Consulta únicamente users/{uid} del usuario autenticado. */
class FirebaseCloudUserRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : CloudUserRepository {
    override suspend fun findMine(): CloudUserSnapshot? {
        val uid = auth.currentUser?.uid
            ?: throw IllegalStateException("Se necesita iniciar sesión")

        val document = firestore.collection("users").document(uid)
            .get().awaitFirebaseTask()
        // Impide devolver el resultado de una sesión que se haya cerrado o cambiado.
        if (auth.currentUser?.uid != uid) {
            throw IllegalStateException("La sesión cambió durante la consulta")
        }
        if (!document.exists()) return null

        return FirestoreUserMapper.map(
            uid = uid,
            fields = document.data.orEmpty(),
            fromCache = document.metadata.isFromCache,
            hasPendingWrites = document.metadata.hasPendingWrites()
        )
    }
}
