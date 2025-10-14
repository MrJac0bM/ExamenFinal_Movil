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



/**
 * Gestor de preferencias de usuario usando DataStore.
 *
 * Proporciona una interfaz type-safe para almacenar y recuperar
 * preferencias del usuario de forma asíncrona y persistente.
 *
 * @property context Contexto de la aplicación
 */

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    companion object {

        /**
         * Guarda el nombre del último país visitado.
         *
         * @param countryName Nombre del país a guardar
         *
         * Almacena el valor de forma asíncrona y segura usando DataStore Preferences.
         * La operación es atómica y maneja la concurrencia automáticamente.
         */
        private val LAST_VISITED_COUNTRY = stringPreferencesKey("last_visited_country")
    }

    /**
     * Flow reactivo del último país visitado.
     *
     * Emite el nombre del último país visitado cada vez que cambia,
     * permitiendo a los observadores reaccionar a cambios en tiempo real.
     */
    suspend fun saveLastVisitedCountry(countryName: String) {
        dataStore.edit { preferences ->
            preferences[LAST_VISITED_COUNTRY] = countryName
        }
    }


    val lastVisitedCountry: Flow<String?> = dataStore.data.map { preferences ->
        preferences[LAST_VISITED_COUNTRY]
    }

    /**
     * Limpia la preferencia del último país visitado.
     *
     * Útil para resetear el estado o en casos de cierre de sesión.
     */
    suspend fun clearLastVisitedCountry() {
        dataStore.edit { preferences ->
            preferences.remove(LAST_VISITED_COUNTRY)
        }
    }
}