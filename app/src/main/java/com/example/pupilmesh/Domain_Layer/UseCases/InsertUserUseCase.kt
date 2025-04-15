package com.example.pupilmesh.Domain_Layer.UseCases

import com.example.pupilmesh.Common.State.State
import com.example.pupilmesh.Data_Layer.Repo.PupilRepo
import com.example.pupilmesh.Domain_Layer.Modal.User
import javax.inject.Inject

class InsertUserUseCase @Inject constructor(
    private val repository: PupilRepo
) {
    suspend operator fun invoke(user: User):State<Unit> {
       return repository.insertUser(user)
    }
}