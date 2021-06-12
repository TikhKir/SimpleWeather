package com.example.simpleweather.domain

import com.example.simpleweather.repository.RepositoryApi
import javax.inject.Inject

class MeasureUnitsTransformUseCase @Inject constructor(
    private val repo: RepositoryApi
) {

//    suspend fun getCurrent(locationId: Long): Flow<Result<CurrentConditionUI>> =
//        withContext(Dispatchers.Default) {
//        repo.getCurrentCondition(locationId).map {
//            it.transformResult { it.toCurrentConditionUI() }
//        }
//    }


}