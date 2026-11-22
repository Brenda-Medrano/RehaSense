package com.rehasense.app.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class ProfileModelTest {
    @Test(expected = IllegalArgumentException::class)
    fun userRejectsBlankId() { User(" ", "Ana", 0L) }

    @Test(expected = IllegalArgumentException::class)
    fun userRejectsBlankName() { User("user-1", " ", 0L) }

    @Test(expected = IllegalArgumentException::class)
    fun userRejectsNegativeTimestamp() { User("user-1", "Ana", -1L) }

    @Test(expected = IllegalArgumentException::class)
    fun patientRejectsBlankPatientId() { PatientProfile("profile-1", "user-1", " ") }

    @Test(expected = IllegalArgumentException::class)
    fun physiotherapistRejectsBlankProfileId() { PhysiotherapistProfile(" ", "user-1", "professional-1") }

    @Test(expected = IllegalArgumentException::class)
    fun profileRejectsBlankUserId() { PatientProfile("profile-1", " ", "patient-1") }

    @Test(expected = IllegalArgumentException::class)
    fun rejectsProfileBelongingToAnotherUser() {
        LocalUserProfile(User("user-1", "Ana", 0L), PatientProfile("profile-1", "user-2", "patient-1"))
    }

    @Test
    fun allowsMatchingUserAndProfile() {
        val user = User("user-1", "Ana", 0L)
        val profile = PhysiotherapistProfile("profile-1", user.id, "professional-1")
        assertEquals(user.id, LocalUserProfile(user, profile).profile.userId)
    }
}
