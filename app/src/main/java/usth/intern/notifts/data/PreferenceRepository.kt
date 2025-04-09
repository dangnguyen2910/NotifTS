package usth.intern.notifts.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val TAG = "PreferenceRepository"

class PreferenceRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    val isActivatedFlow: Flow<Boolean> = dataStore.data
        .map { value: Preferences ->
            value[IS_ACTIVATED] ?: false
        }

    suspend fun updateActivationState(isActivated: Boolean) {
        Log.d(TAG, "Update isActivated = $isActivated")
        dataStore.edit { preferences ->
            preferences[IS_ACTIVATED] = isActivated
        }
    }
}