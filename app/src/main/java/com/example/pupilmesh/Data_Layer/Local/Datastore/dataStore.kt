package com.example.pupilmesh.Data_Layer.Local.Datastore
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_prefs")

    private val dataStore = context.dataStore

    suspend fun setLoginStatus(value: Boolean) {

        dataStore.edit { prefs ->
            prefs[PreferenceKeys.IS_LOGGED_IN] = value
        }
    }

    val isLoggedIn: Flow<Boolean> = dataStore.data
        .map { prefs -> prefs[PreferenceKeys.IS_LOGGED_IN] ?: false }
}
