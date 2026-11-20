package com.rehasense.app.domain.usecase

import com.rehasense.app.domain.model.PatientProfile
import com.rehasense.app.domain.model.PhysiotherapistProfile
import com.rehasense.app.domain.model.UserRole
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertNotEquals
import org.junit.Test

class CreateUserProfileTest {
    private fun creator(): CreateUserProfile {
        val ids = listOf("user-1", "profile-1", "member-1").iterator()
        return CreateUserProfile(newId = { ids.next() }, now = { 123L })
    }

    @Test
    fun createsPatientWithMatchingIdsAndTimestamp() {
        val result = creator()("Ana Perez", UserRole.PATIENT)
        assertEquals("user-1", result.user.id)
        assertEquals("Ana Perez", result.user.name)
        assertEquals(123L, result.user.createdAtEpochMillis)
        assertEquals(PatientProfile("profile-1", "user-1", "member-1"), result.profile)
        assertEquals(UserRole.PATIENT, result.profile.role)
    }

    @Test
    fun createsPhysiotherapistWithMatchingIds() {
        val result = creator()("Alex", UserRole.PHYSIOTHERAPIST)
        assertEquals(PhysiotherapistProfile("profile-1", "user-1", "member-1"), result.profile)
        assertEquals(UserRole.PHYSIOTHERAPIST, result.profile.role)
    }

    @Test
    fun trimsOuterWhitespace() {
        assertEquals("Ana Maria", creator()("  Ana Maria  ", UserRole.PATIENT).user.name)
    }

    @Test(expected = IllegalArgumentException::class)
    fun rejectsEmptyName() { creator()("", UserRole.PATIENT) }

    @Test(expected = IllegalArgumentException::class)
    fun rejectsWhitespaceOnlyName() { creator()(" \t\n ", UserRole.PHYSIOTHERAPIST) }

    @Test
    fun invalidNameDoesNotConsumeIds() {
        var calls = 0
        val create = CreateUserProfile(newId = { calls++; "unused" }, now = { 123L })
        try { create("  ", UserRole.PATIENT) } catch (_: IllegalArgumentException) {
            assertEquals(0, calls)
            return
        }
        throw AssertionError("Expected invalid name to be rejected")
    }

    @Test
    fun defaultIdsAreNonBlankAndDistinctAcrossProfiles() {
        val create = CreateUserProfile()
        val first = create("Ana", UserRole.PATIENT)
        val second = create("Alex", UserRole.PHYSIOTHERAPIST)
        assertTrue(first.user.id.isNotBlank())
        assertTrue(first.profile.id.isNotBlank())
        assertNotEquals(first.user.id, second.user.id)
        assertNotEquals(first.profile.id, second.profile.id)
    }
}
