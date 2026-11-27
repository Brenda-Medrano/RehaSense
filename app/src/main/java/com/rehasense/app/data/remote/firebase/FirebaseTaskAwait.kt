package com.rehasense.app.data.remote.firebase

import com.google.android.gms.tasks.Task
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

/** Adaptador sin dependencia adicional de kotlinx-coroutines-play-services. */
internal suspend fun <T> Task<T>.awaitFirebaseTask(): T =
    suspendCancellableCoroutine { continuation ->
        addOnCompleteListener { completed ->
            if (!continuation.isActive) return@addOnCompleteListener

            if (completed.isSuccessful) {
                continuation.resume(completed.result)
            } else {
                continuation.resumeWithException(
                    completed.exception ?: IllegalStateException("La operación de Firebase falló")
                )
            }
        }
        // La cancelación detiene la espera, pero Task no admite cancelación general.
    }
