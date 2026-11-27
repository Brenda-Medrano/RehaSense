package com.rehasense.app.data.remote.firebase

import com.rehasense.app.domain.model.CloudUserRole
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class FirestoreUserMapperTest {
    @Test
    fun `mapea paciente y conserva indicadores de cache`() {
        val result = FirestoreUserMapper.map(
            uid = "uid-001",
            fields = mapOf("displayName" to "  Ana  ", "role" to "PATIENT"),
            fromCache = true,
            hasPendingWrites = false
        )

        assertEquals("uid-001", result.profile.uid)
        assertEquals("Ana", result.profile.displayName)
        assertEquals(CloudUserRole.PATIENT, result.profile.role)
        assertTrue(result.fromCache)
        assertFalse(result.hasPendingWrites)
    }

    @Test
    fun `mapea fisioterapeuta sin conceder autorizaciones`() {
        val result = FirestoreUserMapper.map(
            uid = "uid-002",
            fields = mapOf("displayName" to "José", "role" to "PHYSIOTHERAPIST"),
            fromCache = false,
            hasPendingWrites = true
        )

        assertEquals(CloudUserRole.PHYSIOTHERAPIST, result.profile.role)
        assertTrue(result.hasPendingWrites)
    }

    @Test
    fun `rechaza roles desconocidos`() {
        try {
            FirestoreUserMapper.map("uid-003", mapOf("role" to "ADMIN"), false, false)
            fail("Un rol no contemplado debe rechazarse")
        } catch (expected: IllegalArgumentException) {
            assertEquals("Rol de perfil desconocido", expected.message)
        }
    }

    @Test
    fun `rechaza datos sin rol`() {
        try {
            FirestoreUserMapper.map("uid-004", mapOf("displayName" to "Ana"), false, false)
            fail("Un rol ausente debe rechazarse")
        } catch (expected: IllegalArgumentException) {
            assertEquals("El perfil no contiene un rol válido", expected.message)
        }
    }
}
