package com.example.pupilmesh.Domain_Layer.UseCases

import com.example.pupilmesh.Common.State.State
import com.example.pupilmesh.Data_Layer.Repo.PupilRepo
import com.example.pupilmesh.Domain_Layer.Modal.Data
import javax.inject.Inject

class GetMangaUseCase @Inject constructor(
    private val pupilRepo: PupilRepo
) {
    suspend operator fun invoke(id: String): State<Data> {
        return pupilRepo.getMangaById(id)
    }
}