package com.example.pupilmesh.Domain_Layer.UseCases

import com.example.pupilmesh.Common.State.State
import com.example.pupilmesh.Data_Layer.Repo.PupilRepo
import com.example.pupilmesh.Domain_Layer.Modal.Manga
import javax.inject.Inject

class GetAllMangaUseCase @Inject constructor(
    private val pupilRepo: PupilRepo
) {
    suspend operator fun invoke(page: Int): State<Manga> {
        return pupilRepo.getAllManga(page)
    }
}
