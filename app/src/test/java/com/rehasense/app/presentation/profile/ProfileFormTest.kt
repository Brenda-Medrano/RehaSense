package com.rehasense.app.presentation.profile

import com.rehasense.app.domain.model.PatientProfile
import com.rehasense.app.domain.model.PhysiotherapistProfile
import com.rehasense.app.domain.model.UserRole
import com.rehasense.app.domain.usecase.CreateUserProfile
import org.junit.Assert.*
import org.junit.Test

class ProfileFormTest {
    private var ids = 0
    private val form = ProfileForm(CreateUserProfile(newId = { "id-${++ids}" }, now = { 100L }))

    @Test
    fun initialStateHasNoProfileOrError() {
        val state = ProfileUiState()
        assertEquals("", state.name)
        assertFalse(state.showNameError)
        assertNull(state.profile)
    }

    @Test
    fun blankNameIsRejectedWithoutCreatingIdentifiers() {
        val state = form.submit(ProfileUiState(name = "   "), UserRole.PATIENT)
        assertTrue(state.showNameError)
        assertNull(state.profile)
        assertEquals(0, ids)
    }

    @Test
    fun editingClearsThePreviousError() {
        val invalid = form.submit(ProfileUiState(), UserRole.PATIENT)
        val edited = form.editName(invalid, "Ana")
        assertEquals("Ana", edited.name)
        assertFalse(edited.showNameError)
    }

    @Test
    fun patientUsesDomainProfileAndNormalizedName() {
        val state = form.submit(ProfileUiState(name = "  Ana Pérez  "), UserRole.PATIENT)
        val profile = requireNotNull(state.profile)
        assertEquals("Ana Pérez", profile.user.name)
        assertTrue(profile.profile is PatientProfile)
        assertEquals(profile.user.id, profile.profile.userId)
        assertEquals(100L, profile.user.createdAtEpochMillis)
        assertFalse(state.showNameError)
    }

    @Test
    fun physiotherapistUsesCorrectDomainRole() {
        val state = form.submit(ProfileUiState(name = "Carlos"), UserRole.PHYSIOTHERAPIST)
        val profile = requireNotNull(state.profile)
        assertTrue(profile.profile is PhysiotherapistProfile)
        assertEquals(UserRole.PHYSIOTHERAPIST, profile.profile.role)
    }

    @Test
    fun repeatedSubmitDoesNotCreateADuplicateProfile() {
        val first = form.submit(ProfileUiState(name = "Ana"), UserRole.PATIENT)
        val count = ids
        val second = form.submit(first, UserRole.PATIENT)
        assertSame(first, second)
        assertEquals(count, ids)
    }

    @Test
    fun createdProfileCannotBeEditedThroughTheRegistrationForm() {
        val created = form.submit(ProfileUiState(name = "Ana"), UserRole.PATIENT)
        assertSame(created, form.editName(created, "Other"))
    }
}
