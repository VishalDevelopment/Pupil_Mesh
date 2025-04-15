package com.example.pupilmesh.Domain_Layer.UseCases

import com.example.pupilmesh.Data_Layer.Repo.PupilRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLoginStatus @Inject constructor(
    private val pupilRepo: PupilRepo
) {
    operator fun invoke(): Flow<Boolean> = pupilRepo.getLoginStatus()
}