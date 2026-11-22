package com.rehasense.app.domain.usecase

import com.rehasense.app.domain.model.LocalUserProfile
import com.rehasense.app.domain.model.PatientProfile
import com.rehasense.app.domain.model.PhysiotherapistProfile
import com.rehasense.app.domain.model.User
import com.rehasense.app.domain.model.UserRole
import java.util.UUID

class CreateUserProfile(
    private val newId: () -> String = { UUID.randomUUID().toString() },
    private val now: () -> Long = System::currentTimeMillis
) {
    operator fun invoke(name: String, role: UserRole): LocalUserProfile {
        val normalizedName = name.trim()
        require(normalizedName.isNotEmpty()) { "Name must not be blank" }
        val user = User(newId(), normalizedName, now())
        val profile = when (role) {
            UserRole.PATIENT -> PatientProfile(newId(), user.id, newId())
            UserRole.PHYSIOTHERAPIST -> PhysiotherapistProfile(newId(), user.id, newId())
        }
        return LocalUserProfile(user, profile)
    }
}
