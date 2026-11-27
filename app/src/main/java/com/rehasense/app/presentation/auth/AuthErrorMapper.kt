package com.rehasense.app.presentation.auth

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

/** Mensajes seguros para la UI; no mostrar trazas ni mensajes internos de Firebase. */
internal fun authErrorMessage(error: Throwable): String = when (error) {
    is FirebaseNetworkException -> "No hay conexión. Comprueba Internet y vuelve a intentarlo."
    is FirebaseTooManyRequestsException -> "Demasiados intentos. Vuelve a intentarlo más tarde."
    is FirebaseAuthUserCollisionException -> "No se pudo registrar esa cuenta. Comprueba el correo o inicia sesión."
    is FirebaseAuthWeakPasswordException -> "La contraseña no cumple la política de seguridad."
    is FirebaseAuthInvalidCredentialsException -> "No se pudo autenticar. Revisa las credenciales."
    else -> "No se pudo completar la operación. Vuelve a intentarlo."
}
