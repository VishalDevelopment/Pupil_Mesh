package com.example.pupilmesh.Data_Layer.Local.Room.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "manga_table")
data class MangaEntity(
    @PrimaryKey val id: String,
    val title: String,
    val sub_title: String,
    val thumb: String,
    val summary: String,
    val authors: String,
    val genres: String,
    val type: String,
    val status: String,
    val nsfw: Boolean,
    val total_chapter: Int,
    val create_at: Long,
    val update_at: Long
)
