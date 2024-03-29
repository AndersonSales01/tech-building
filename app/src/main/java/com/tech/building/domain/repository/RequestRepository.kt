package com.tech.building.domain.repository

import com.tech.building.domain.model.FilterRequestStatus
import com.tech.building.domain.model.MaterialModel
import com.tech.building.domain.model.RequestModel
import kotlinx.coroutines.flow.Flow

interface RequestRepository {
    fun sendRequest(requestModel: RequestModel): Flow<Unit>
    fun getRequestsFiltered(
        registrationCollaborator: String,
        filter: FilterRequestStatus
    ): Flow<List<RequestModel>>
    fun releaseRequest(requestModel: RequestModel): Flow<Unit>
}