package com.rehasense.app.domain.model

data class LocalUserProfile(val user: User, val profile: UserProfile) {
    init {
        require(user.id == profile.userId) { "Profile must belong to its user" }
    }
}
