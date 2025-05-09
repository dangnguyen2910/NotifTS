package usth.intern.notifts.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
val IS_ACTIVATED = booleanPreferencesKey("speaker_activated")
val SPEAKER_IS_ENABLED_WHEN_SCREEN_ON = booleanPreferencesKey("speaker_is_enabled_when_screen_on")
val SPEAKER_IS_ENABLED_WHEN_DND_ON = booleanPreferencesKey("speaker_is_enabled_when_dnd_on")
val NOTIFICATION_IS_SHOWN = booleanPreferencesKey("notification_is_shown")
