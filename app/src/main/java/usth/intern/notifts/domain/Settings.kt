package usth.intern.notifts.domain

import android.content.Context
import android.os.PowerManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class Settings @Inject constructor(
    @ApplicationContext private val context: Context
){
    fun isScreenOn() : Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return powerManager.isInteractive
    }
}