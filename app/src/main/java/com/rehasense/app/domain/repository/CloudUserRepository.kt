package com.rehasense.app.domain.repository

import com.rehasense.app.domain.model.CloudUserSnapshot

/** Solo lee el documento del usuario autenticado; no expone lecturas arbitrarias. */
interface CloudUserRepository {
    /** null indica que aún no existe users/{uid}; no significa permiso denegado. */
    suspend fun findMine(): CloudUserSnapshot?
}
