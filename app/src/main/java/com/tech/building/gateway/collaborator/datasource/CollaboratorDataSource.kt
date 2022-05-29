package com.tech.building.gateway.collaborator.datasource

import com.tech.building.domain.model.CollaboratorModel
import kotlinx.coroutines.flow.Flow

interface CollaboratorDataSource {
    fun getCollaborators(): Flow<List<CollaboratorModel>>
}