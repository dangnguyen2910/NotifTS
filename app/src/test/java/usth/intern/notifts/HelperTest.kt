package usth.intern.notifts

import org.junit.Assert.assertEquals
import org.junit.Test
import usth.intern.notifts.domain.getNewApps

class HelperTest {
    @Test
    fun getNewAppsTest() {
        val deviceAppList = listOf("Gmail", "Settings", "Youtube", "Settings", "Facebook")
        val databaseAppList = listOf("Gmail", "Settings", "Settings")
        val newAppsList = getNewApps(deviceAppList, databaseAppList)
        val expected = listOf("Youtube", "Facebook")
        assertEquals(expected, newAppsList)
    }
}