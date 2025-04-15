package com.example.pupilmesh.Data_Layer.Repo

import android.provider.ContactsContract.Data
import com.example.pupilmesh.Common.State.State
import com.example.pupilmesh.Domain_Layer.Modal.Manga
import com.example.pupilmesh.Domain_Layer.Modal.User
import kotlinx.coroutines.flow.Flow

interface PupilRepo {
    suspend fun insertUser(user: User): State<Unit>
    suspend fun UserExist(email: String, password: String): State<Boolean>
    suspend fun saveLoginStatus(isLoggedIn: Boolean)
    fun getLoginStatus(): Flow<Boolean>
     suspend fun getAllManga(page: Int): State<Manga>
    suspend fun getMangaById(id: String): State<com.example.pupilmesh.Domain_Layer.Modal.Data>

}