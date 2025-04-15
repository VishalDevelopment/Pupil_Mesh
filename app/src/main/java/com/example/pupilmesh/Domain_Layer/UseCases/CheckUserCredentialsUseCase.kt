package com.example.pupilmesh.Domain_Layer.UseCases

import com.example.pupilmesh.Common.State.State
import com.example.pupilmesh.Data_Layer.Repo.PupilRepo
import javax.inject.Inject

class CheckUserCredentialsUseCase @Inject constructor(
    private val pupilRepo: PupilRepo
) {
    suspend operator fun invoke(email: String, password: String): State<Boolean> {
        return pupilRepo.UserExist(email, password)
    }
}