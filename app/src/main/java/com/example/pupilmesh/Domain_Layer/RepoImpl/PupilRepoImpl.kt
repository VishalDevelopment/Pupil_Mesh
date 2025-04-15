package com.example.pupilmesh.Domain_Layer.RepoImpl

import android.provider.ContactsContract
import android.util.Log
import com.example.pupilmesh.Common.State.State
import com.example.pupilmesh.Data_Layer.Dto.DataDto
import com.example.pupilmesh.Data_Layer.Local.Datastore.DataStoreManager
import com.example.pupilmesh.Data_Layer.Local.Room.Dao.PupilDao
import com.example.pupilmesh.Data_Layer.Local.Room.Entity.MangaEntity
import com.example.pupilmesh.Data_Layer.Local.Room.Entity.UserEntity
import com.example.pupilmesh.Data_Layer.Remote.MangaApiService
import com.example.pupilmesh.Data_Layer.Repo.PupilRepo
import com.example.pupilmesh.Domain_Layer.Modal.Data
import com.example.pupilmesh.Domain_Layer.Modal.User
//import com.example.pupilmesh.Data_Layer.Mapper.toDomain
import com.example.pupilmesh.Domain_Layer.Modal.Manga
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PupilRepoImpl @Inject constructor(
    private val dao: PupilDao,
    private val dataStoreManager: DataStoreManager,
    private val apiService: MangaApiService

): PupilRepo {
    override suspend fun insertUser(user: User): State<Unit> {
        return try {
            dao.insertUser(UserEntity(email = user.email, password = user.password))
            State.Success(Unit)
        } catch (e: Exception) {
            State.Error(e.localizedMessage!!)
        }
    }

    override suspend fun UserExist(email: String, password: String): State<Boolean> {
        return try {

            val exists = dao.UserExist(email, password)
            Log.d("USER", "Exist : $exists")
            if (exists) {
                saveLoginStatus(true)
                State.Success(true)
            } else {
                State.Success(false)
            }
        } catch (e: Exception) {
            State.Error(e.localizedMessage .toString())
        }
    }

    override suspend fun saveLoginStatus(isLoggedIn: Boolean) {
        dataStoreManager.setLoginStatus(isLoggedIn)
    }

    override fun getLoginStatus(): Flow<Boolean> {
        Log.d("DATASTORE", "LoginStatusFetch: ")
        return dataStoreManager.isLoggedIn
    }

    override suspend fun getAllManga(page: Int): State<Manga> {
        return try {
            val mangaResponse = apiService.getLatestManga(
                page = page,
                genres = "",
                nsfw = true,
                type = "all"
            )

            val mangaEntities = mangaResponse.data.map {
                MangaEntity(
                    id = it.id,
                    title = it.title,
                    sub_title = it.sub_title,
                    thumb = it.thumb,
                    summary = it.summary,
                    authors = it.authors.joinToString(","),
                    genres = it.genres.joinToString(","),
                    type = it.type,
                    status = it.status,
                    nsfw = it.nsfw,
                    total_chapter = it.total_chapter,
                    create_at = it.create_at,
                    update_at = it.update_at
                )
            }

            dao.insertMangaList(mangaEntities)

            State.Success(mangaResponse)
        } catch (e: Exception) {
            val localData = dao.getAllManga()
            val localManga = Manga(
                code = 0,
                data = localData.map {
                    Data(
                        authors = it.authors.split(","),
                        create_at = it.create_at,
                        genres = it.genres.split(","),
                        id = it.id,
                        nsfw = it.nsfw,
                        status = it.status,
                        sub_title = it.sub_title,
                        summary = it.summary,
                        thumb = it.thumb,
                        title = it.title,
                        total_chapter = it.total_chapter,
                        type = it.type,
                        update_at = it.update_at
                    )
                }
            )

            State.Success(localManga)
        }
    }

    override suspend fun getMangaById(id: String): State<Data> {
            return try {
                val response = apiService.getMangaDetail(id)
                Log.d("REPSONSE","$response")

                State.Success(response)
            } catch (e: Exception) {
                State.Error(e.localizedMessage.toString() )
            }
        }



}