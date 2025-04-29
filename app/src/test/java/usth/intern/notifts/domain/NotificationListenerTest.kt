package usth.intern.notifts.domain

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class NotificationListenerTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun isAllowedToSpeak_NotActivated_ReturnFalse() {
        val notificationListener = Robolectric.setupService(NotificationListener::class.java)
        val isAllowedToSpeak = notificationListener.isAllowedToSpeak(
            isActivated = false,
            isDuplicated = false,
            speakerIsActivatedWhenScreenOn = false
        )

        assertEquals(isAllowedToSpeak, false)
    }

    @Test
    fun isAllowedToSpeak_Duplicated_ReturnFalse() {
        val notificationListener = Robolectric.setupService(NotificationListener::class.java)
        val isAllowedToSpeak = notificationListener.isAllowedToSpeak(
            isActivated = true,
            isDuplicated = true,
            speakerIsActivatedWhenScreenOn = false
        )

        assertEquals(isAllowedToSpeak, false)
    }

    @Test
    fun isAllowedToSpeak_IsActivatedAndNotDuplicatedAndScreenNotMatter_ReturnTrue() {
        val notificationListener = Robolectric.setupService(NotificationListener::class.java)
        val isAllowedToSpeak = notificationListener.isAllowedToSpeak(
            isActivated = true,
            isDuplicated = false,
            speakerIsActivatedWhenScreenOn = true
        )

        assertEquals(isAllowedToSpeak, true)
    }
}