package com.example.pupilmesh.Data_Layer.Remote

import com.example.pupilmesh.Data_Layer.Dto.DataDto
import com.example.pupilmesh.Data_Layer.Dto.MangaDto
import com.example.pupilmesh.Domain_Layer.Modal.Data
import com.example.pupilmesh.Domain_Layer.Modal.Manga
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface MangaApiService {
    @GET("manga/latest")
    @Headers(
        "X-Rapidapi-Key: bc226463a4msh8c8fcd5df51a0dcp1af446jsncce3d5409e0c",
        "X-Rapidapi-Host: mangaverse-api.p.rapidapi.com"
    )
    suspend fun getLatestManga(
        @Query("page") page: Int,
        @Query("genres") genres: String,
        @Query("nsfw") nsfw: Boolean,
        @Query("type") type: String = "all"
    ): Manga



    @Headers(
        "X-Rapidapi-Key: bc226463a4msh8c8fcd5df51a0dcp1af446jsncce3d5409e0c",
        "X-Rapidapi-Host: mangaverse-api.p.rapidapi.com"
    )
    @GET("manga")
   suspend fun getMangaDetail(
        @Query("id") id: String
    ):Data

}