package usth.intern.notifts.data.repository

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import usth.intern.notifts.data.ENGLISH_VOICE
import usth.intern.notifts.data.FRENCH_VOICE
import usth.intern.notifts.data.IS_ACTIVATED
import usth.intern.notifts.data.IS_FIRST_TIME
import usth.intern.notifts.data.NOTIFICATION_IS_SHOWN
import usth.intern.notifts.data.SPEAKER_IS_ENABLED_WHEN_DND_ON
import usth.intern.notifts.data.SPEAKER_IS_ENABLED_WHEN_SCREEN_ON
import usth.intern.notifts.data.dataStore
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

    val isFirstTime: Flow<Boolean> = dataStore.data.map {
        it[IS_FIRST_TIME] ?: true
    }


    val englishVoice: Flow<String> = dataStore.data.map { it[ENGLISH_VOICE] ?: "Heart (F)" }
    val frenchVoice: Flow<String> = dataStore.data.map { it[FRENCH_VOICE] ?: "Siwis (F)" }

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

    suspend fun updateEnglishVoice(voice: String) {
        dataStore.edit { it[ENGLISH_VOICE] = voice }
    }

    suspend fun updateIsFirstTime() {
        dataStore.edit { it[IS_FIRST_TIME] = false }
    }
}