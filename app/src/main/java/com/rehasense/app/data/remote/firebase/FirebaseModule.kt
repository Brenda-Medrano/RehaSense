package com.rehasense.app.data.remote.firebase

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rehasense.app.domain.repository.AuthRepository
import com.rehasense.app.domain.repository.CloudUserRepository

/** Punto de composición para inyectar Firebase sin alterar RehaSenseGraph ni Room. */
class FirebaseModule(
    auth: FirebaseAuth,
    firestore: FirebaseFirestore
) {
    val authRepository: AuthRepository = FirebaseAuthRepository(auth)
    val cloudUserRepository: CloudUserRepository = FirebaseCloudUserRepository(auth, firestore)

    companion object {
        /** Requiere que Google Services haya inicializado FirebaseApp. */
        fun fromDefaultApp(): FirebaseModule {
            val app = FirebaseApp.getInstance()
            return FirebaseModule(
                auth = FirebaseAuth.getInstance(app),
                firestore = FirebaseFirestore.getInstance(app)
            )
        }
    }
}
