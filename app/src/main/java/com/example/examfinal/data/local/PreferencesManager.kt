package com.example.examfinal.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "country_preferences")

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    companion object {
        private val LAST_VISITED_COUNTRY = stringPreferencesKey("last_visited_country")
    }

    suspend fun saveLastVisitedCountry(countryName: String) {
        dataStore.edit { preferences ->
            preferences[LAST_VISITED_COUNTRY] = countryName
        }
    }


    val lastVisitedCountry: Flow<String?> = dataStore.data.map { preferences ->
        preferences[LAST_VISITED_COUNTRY]
    }


    suspend fun clearLastVisitedCountry() {
        dataStore.edit { preferences ->
            preferences.remove(LAST_VISITED_COUNTRY)
        }
    }
}