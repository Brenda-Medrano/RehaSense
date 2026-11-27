package com.rehasense.app.data.remote.firebase

import com.rehasense.app.domain.model.CloudUserProfile
import com.rehasense.app.domain.model.CloudUserRole
import com.rehasense.app.domain.model.CloudUserSnapshot

/** Conversión pura: no consulta Firebase ni interpreta el rol como un permiso. */
internal object FirestoreUserMapper {
    fun map(
        uid: String,
        fields: Map<String, Any?>,
        fromCache: Boolean,
        hasPendingWrites: Boolean
    ): CloudUserSnapshot {
        require(uid.isNotBlank()) { "Identificador de usuario inválido" }
        val displayName = (fields["displayName"] as? String)?.trim().orEmpty()
        val roleName = fields["role"] as? String
            ?: throw IllegalArgumentException("El perfil no contiene un rol válido")
        val role = CloudUserRole.entries.firstOrNull { it.name == roleName }
            ?: throw IllegalArgumentException("Rol de perfil desconocido")

        return CloudUserSnapshot(
            profile = CloudUserProfile(uid, displayName, role),
            fromCache = fromCache,
            hasPendingWrites = hasPendingWrites
        )
    }
}
