package usth.intern.notifts.domain

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

class NotificationListenerTest {

    private lateinit var notificationListener: NotificationListener

    @Before
    fun setup() {
        notificationListener = NotificationListener()
    }

    @Test
    fun isAllowedToSpeak_NotActivated_ttsEngineNotRun() {
        val isAllowedToSpeak = notificationListener.isAllowedToSpeak(
            isActivated = false,
            isDuplicated = false,
            speakerIsActivatedWhenScreenOn = false
        )

        assertEquals(isAllowedToSpeak, false)
    }

    @Test
    fun isAllowedToSpeak_Duplicated_ttsEngineNotRun() {
        val isAllowedToSpeak = notificationListener.isAllowedToSpeak(
            isActivated = true,
            isDuplicated = true,
            speakerIsActivatedWhenScreenOn = false
        )

        assertEquals(isAllowedToSpeak, false)
    }

    @Ignore("Not implemented")
    @Test
    fun isAllowedToSpeak_IsActivatedAndNotDuplicatedAnd_ttsEngineNotRun() {
        val isAllowedToSpeak = notificationListener.isAllowedToSpeak(
            isActivated = true,
            isDuplicated = false,
            speakerIsActivatedWhenScreenOn = true
        )

        assertEquals(isAllowedToSpeak, true)
    }
}