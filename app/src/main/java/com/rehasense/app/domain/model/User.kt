package com.rehasense.app.domain.model

data class User(val id: String, val name: String, val createdAtEpochMillis: Long) {
    init {
        require(id.isNotBlank()) { "User ID must not be blank" }
        require(name.isNotBlank()) { "Name must not be blank" }
        require(createdAtEpochMillis >= 0L) { "Creation timestamp must not be negative" }
    }
}
