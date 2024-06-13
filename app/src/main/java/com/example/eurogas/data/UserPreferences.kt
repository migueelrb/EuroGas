package com.example.eurogas.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.userDataStore by preferencesDataStore(UserPreferences.SETTINGS_FILE)

data class UserPreferences(
    val codPostal: String = "",
) {
    companion object {
        const val SETTINGS_FILE = "user_preferences"
        val COD_POSTAL = stringPreferencesKey("codPostal")
    }
}

/**
 * Función de extensión para obtener las preferencias del usuario desde el DataStore
 * @return Flow<UserPreferences> Flujo de preferencias del usuario
 */
fun Context.getUserPreferences(): Flow<UserPreferences> {
    val dataStore = userDataStore
    return dataStore.data.map { preferences ->
        UserPreferences(
            codPostal = preferences[UserPreferences.COD_POSTAL] ?: "",
        )
    }
}

/**
 * Función de extensión para guardar las preferencias del usuario en el DataStore
 * @param userPreferences Preferencias del usuario a guardar
 */
suspend fun Context.saveUserPreferences(userPreferences: UserPreferences) {
    userDataStore.edit { preferences ->
        preferences[UserPreferences.COD_POSTAL] = userPreferences.codPostal
    }
}
