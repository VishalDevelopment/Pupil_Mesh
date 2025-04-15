package com.example.pupilmesh.Common.Di

import android.content.Context
import androidx.room.Room
import com.example.pupilmesh.Data_Layer.Local.Datastore.DataStoreManager
import com.example.pupilmesh.Data_Layer.Local.Room.Dao.PupilDao
import com.example.pupilmesh.Data_Layer.Local.Room.Database.PupilDatabase
import com.example.pupilmesh.Data_Layer.Remote.MangaApiService
import com.example.pupilmesh.Data_Layer.Repo.PupilRepo
import com.example.pupilmesh.Domain_Layer.RepoImpl.PupilRepoImpl
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


    @Module
    @InstallIn(SingletonComponent::class)
    object AppModule {
        @Provides
        @Singleton
        fun provideRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://mangaverse-api.p.rapidapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @Provides
        @Singleton
        fun provideMangaApiService(retrofit: Retrofit): MangaApiService {
            return retrofit.create(MangaApiService::class.java)
        }

        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): PupilDatabase =
            Room.databaseBuilder(context, PupilDatabase::class.java, "user_db").build()

        @Provides
        fun provideUserDao(db: PupilDatabase): PupilDao = db.dao()

        @Provides
        @Singleton
        fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager =
            DataStoreManager(context)

        @Provides
        @Singleton
        fun provideUserRepository(PupilDao: PupilDao , dataStoreManager: DataStoreManager,apiService: MangaApiService): PupilRepo =
            PupilRepoImpl(PupilDao , dataStoreManager,apiService)
    }