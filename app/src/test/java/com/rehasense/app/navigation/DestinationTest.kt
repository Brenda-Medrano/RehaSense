package com.rehasense.app.navigation

import com.rehasense.app.domain.model.UserRole
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DestinationTest {
    @Test
    fun patientOpensPatientHome() {
        assertEquals(Destination.PATIENT_HOME, Destination.homeFor(UserRole.PATIENT))
    }

    @Test
    fun physiotherapistOpensProfessionalHome() {
        assertEquals(Destination.PHYSIOTHERAPIST_HOME, Destination.homeFor(UserRole.PHYSIOTHERAPIST))
    }

    @Test
    fun allRoutesAreUniqueAndNonBlank() {
        val routes = Destination.values().map { it.route }
        assertTrue(routes.all { it.isNotBlank() })
        assertEquals(routes.size, routes.toSet().size)
    }
}
