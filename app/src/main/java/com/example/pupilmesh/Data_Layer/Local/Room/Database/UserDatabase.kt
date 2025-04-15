package com.example.pupilmesh.Data_Layer.Local.Room.Database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pupilmesh.Data_Layer.Local.Room.Dao.PupilDao
import com.example.pupilmesh.Data_Layer.Local.Room.Entity.MangaEntity
import com.example.pupilmesh.Data_Layer.Local.Room.Entity.UserEntity

@Database(entities = [UserEntity::class , MangaEntity::class], version = 2 , exportSchema = false)
abstract class PupilDatabase : RoomDatabase() {
    abstract fun dao(): PupilDao

}