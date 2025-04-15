package com.example.pupilmesh.Data_Layer.Local.Room.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pupilmesh.Data_Layer.Local.Room.Entity.MangaEntity
import com.example.pupilmesh.Data_Layer.Local.Room.Entity.UserEntity

@Dao
interface PupilDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)
    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE email = :email AND password = :password LIMIT 1)")
    suspend fun UserExist(email: String, password: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMangaList(mangaList: List<MangaEntity>)

    @Query("SELECT * FROM manga_table")
    suspend fun getAllManga(): List<MangaEntity>
}