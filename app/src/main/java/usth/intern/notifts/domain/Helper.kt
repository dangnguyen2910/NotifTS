package usth.intern.notifts.domain

import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.PowerManager
import android.util.Log

fun isWifiConnected(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
}

fun hasInternetConnection(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}

fun getAppName(context: Context, packageName: String): String? {
    return try {
        val packageManager = context.packageManager
        val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
        packageManager.getApplicationLabel(applicationInfo).toString()
    } catch (e: PackageManager.NameNotFoundException) {
        Log.e("AppName", "Package not found: $packageName")
        null // Package not found
    }
}

fun isScreenOn(context: Context) : Boolean {
    val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    return powerManager.isInteractive
}

fun getNewApps(
    deviceAppList: List<String>,
    databaseAppList: List<String>,
) : List<String> {
    val databaseAppCountMap = databaseAppList.groupingBy{ it }.eachCount().toMutableMap()
    val newAppList = mutableListOf<String>()

    deviceAppList.forEach { app ->
        val count = databaseAppCountMap[app] ?: 0
        if (count > 0) {
            databaseAppCountMap[app] = count - 1
        } else {
            newAppList.add(app)
        }
    }
    return newAppList
}