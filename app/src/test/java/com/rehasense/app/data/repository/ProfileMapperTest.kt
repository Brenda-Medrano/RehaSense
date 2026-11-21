package com.rehasense.app.data.repository

import com.rehasense.app.domain.model.UserRole
import com.rehasense.app.domain.usecase.CreateUserProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ProfileMapperTest {
    private fun example(role: UserRole) = CreateUserProfile(
        newId = sequenceOf("user", "profile", "role").iterator()::next,
        now = { 42L }
    )("Prueba", role)

    @Test fun patientMapsToThreeLinkedRowsAndBack() {
        val original = example(UserRole.PATIENT)
        val rows = original.toProfileRows()
        assertEquals("user", rows.profile.userId)
        assertEquals("profile", rows.patient?.profileId)
        assertNull(rows.physiotherapist)
        assertEquals(original, rows.toDomain())
    }

    @Test fun therapistMapsToThreeLinkedRowsAndBack() {
        val original = example(UserRole.PHYSIOTHERAPIST)
        val rows = original.toProfileRows()
        assertEquals("profile", rows.physiotherapist?.profileId)
        assertNull(rows.patient)
        assertEquals(original, rows.toDomain())
    }
}
