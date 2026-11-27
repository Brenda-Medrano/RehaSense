package com.rehasense.app.presentation.auth

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AuthFormValidatorTest {
    @Test fun `rechaza correo vacio`() {
        assertEquals("Introduce tu correo electrónico.", AuthFormValidator.email("  "))
    }

    @Test fun `rechaza correo con formato invalido`() {
        assertEquals("Introduce un correo válido.", AuthFormValidator.email("no-es-correo"))
        assertEquals("Introduce un correo válido.", AuthFormValidator.email("ana @ejemplo.com"))
    }

    @Test fun `permite correo valido con espacios alrededor`() {
        assertNull(AuthFormValidator.email("  ana@ejemplo.com  "))
    }

    @Test fun `inicio exige contrasena`() {
        assertEquals("Introduce tu contraseña.", AuthFormValidator.signIn("ana@ejemplo.com", ""))
    }

    @Test fun `registro exige al menos seis caracteres`() {
        assertEquals(
            "La contraseña debe tener al menos 6 caracteres.",
            AuthFormValidator.register("ana@ejemplo.com", "12345", "12345")
        )
    }

    @Test fun `registro exige confirmar contrasena`() {
        assertEquals(
            "Las contraseñas no coinciden.",
            AuthFormValidator.register("ana@ejemplo.com", "123456", "654321")
        )
    }

    @Test fun `registro correcto no produce errores`() {
        assertNull(AuthFormValidator.register("ana@ejemplo.com", "123456", "123456"))
    }
}
