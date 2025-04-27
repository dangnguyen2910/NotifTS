package usth.intern.notifts.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferenceRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    val isActivatedFlow: Flow<Boolean> = dataStore.data
        .map { value: Preferences ->
            value[IS_ACTIVATED] ?: false
        }

    val speakerIsEnabledWhenScreenOnFlow: Flow<Boolean> = dataStore.data
        .map { value: Preferences ->
            value[SPEAKER_IS_ENABLED_WHEN_SCREEN_ON] ?: false
        }

    val speakerIsEnabledWhenDndOnFlow: Flow<Boolean> = dataStore.data
        .map { value: Preferences ->
            value[SPEAKER_IS_ENABLED_WHEN_DND_ON] ?: false
        }
    val notificationIsShownFlow: Flow<Boolean> = dataStore.data
        .map { value: Preferences ->
            value[NOTIFICATION_IS_SHOWN] ?: true
        }

    suspend fun updateActivationState(isActivated: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_ACTIVATED] = isActivated
        }
    }

    suspend fun updateScreenOnActivationState(speakerIsEnableWhenScreenOn: Boolean) {
        dataStore.edit { preferences ->
            preferences[SPEAKER_IS_ENABLED_WHEN_SCREEN_ON] = speakerIsEnableWhenScreenOn
        }
    }

    suspend fun updateDndOnActivationState(speakerIsEnableWhenDndOn: Boolean) {
        dataStore.edit { preferences ->
            preferences[SPEAKER_IS_ENABLED_WHEN_DND_ON] = speakerIsEnableWhenDndOn
        }
    }

    suspend fun updateNotificationOnActivationState(notificationIsShown: Boolean) {
        dataStore.edit { preferences ->
            preferences[NOTIFICATION_IS_SHOWN] = notificationIsShown
        }
    }
}