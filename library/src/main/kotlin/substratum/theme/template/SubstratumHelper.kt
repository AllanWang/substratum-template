package substratum.theme.template

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import java.io.File

/**
 * Helper file for substratum. None of these should need to be modified by the themer
 */
const val SUBSTRATUM_PACKAGE_NAME = "projekt.substratum"
const val SUBSTRATUM_TAG = "Substratum"
const val SUBSTRATUM_MINIMUM_VERSION = 712
const val SUBSTRATUM_PLAY_STORE_LINK = "https://play.google.com/store/apps/details?id=projekt.substratum"

internal fun log(text: String) = Log.e(SUBSTRATUM_TAG, text)

val BLACKLISTED_APPLICATIONS = arrayOf(
        "com.android.vending.billing.InAppBillingService.LOCK",
        "com.android.vending.billing.InAppBillingService.LACK",
        "cc.madkite.freedom",
        "zone.jasi2169.uretpatcher",
        "uret.jasi2169.patcher",
        "com.dimonvideo.luckypatcher",
        "com.chelpus.lackypatch",
        "com.forpda.lp",
        "com.android.vending.billing.InAppBillingService.LUCK",
        "com.android.protips"
)

val THEME_READY_PACKAGES = arrayOf(
        "com.google.android.gm",
        "com.google.android.googlequicksearchbox",
        "com.android.vending",
        "com.google.android.apps.plus",
        "com.google.android.talk",
        "com.google.android.youtube",
        "com.google.android.inputmethod.latin"
)

val OTHER_THEME_SYSTEMS = arrayOf(
        "com.slimroms.thememanager",
        "com.slimroms.omsbackend"
)

inline val Context.isNetworkAvailable: Boolean
    get() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo?.isConnectedOrConnecting ?: false
    }

fun Context.isPackageInstalled(packageName: String): Boolean {
    try {
        packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        return true
    } catch (e: PackageManager.NameNotFoundException) {
        return false
    }
}

inline val Context.hasOtherThemeSystem: Boolean
    get() = OTHER_THEME_SYSTEMS.any {
        try {
            packageManager.getApplicationInfo(it, 0).enabled
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

inline val Context.hasUpdatedSubstratum: Boolean
    get() = try {
        val info = packageManager.getPackageInfo(SUBSTRATUM_PACKAGE_NAME, 0)
        (info.versionCode >= SUBSTRATUM_MINIMUM_VERSION)
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }

fun Context.substratumDialog(builder: AlertDialog.Builder.() -> Unit) = AlertDialog.Builder(this).apply {
    setIcon(R.mipmap.ic_launcher)
    builder()
}.show()


fun Activity.detectThemeReady() {
    val addon = File("/system/addon.d/80-ThemeReady.sh")
    if (addon.exists()) {
        val apps = mutableListOf<String>()
        var updated = false
        var incomplete = false
        val appName = StringBuilder()

        THEME_READY_PACKAGES.forEach {
            try {
                val appInfo = packageManager.getApplicationInfo(it, 0)
                if (appInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP > 0)
                    apps.add(packageManager.getApplicationLabel(appInfo).toString())
            } catch (e: PackageManager.NameNotFoundException) {
                // Package not found
            }
        }

        if (apps.isEmpty()) {
            calibrateSystem()
            return
        }

        if (apps.size == 1) {
            appName.append(apps.first())
        } else {
            (0 until apps.size - 1).forEach { appName.append(apps[it]).append(", ") }
            appName.append(", ").append(getString(R.string.and)).append(" ").append(apps.last())
        }

//        val message = String.format(getString())
////TODO figure out why you guys have so many toggles
//        if (!updated && !incomplete) {
//            calibrateSystem()
//        } else {
//            val stringInt = if (incomplete)
//                R.string.theme_ready_incomplete
//            else
//                R.string.theme_ready_updated
//            val parse = String.format(getString(stringInt),
//                    appName)
//
//            AlertDialog.Builder(this, R.style.DialogStyle)
//                    .setIcon(R.mipmap.ic_launcher)
//                    .setTitle(getString(R.string.ThemeName))
//                    .setMessage(parse)
//                    .setPositiveButton(R.string.yes) { _, _ -> calibrateSystem() }
//                    .setNegativeButton(R.string.no) { _, _ -> finish() }
//                    .setOnCancelListener { finish() }
//                    .show()
//        }
    } else {
//        AlertDialog.Builder(this, R.style.DialogStyle)
//                .setIcon(R.mipmap.ic_launcher)
//                .setTitle(getString(R.string.ThemeName))
//                .setMessage(getString(R.string.theme_ready_not_detected))
//                .setPositiveButton(R.string.yes) { _, _ -> calibrateSystem() }
//                .setNegativeButton(R.string.no) { _, _ -> finish() }
//                .setOnCancelListener { finish() }
//                .show()
    }
}

fun Context.calibrateSystem() {

}

fun Context.substratumLaunchPlayStore() {
    val i = Intent(Intent.ACTION_VIEW)
    i.data = Uri.parse(SUBSTRATUM_PLAY_STORE_LINK)
    Toast.makeText(this, getString(R.string.toast_substratum), Toast.LENGTH_LONG).show()
    startActivity(i)
    (this as? Activity)?.finish()
}

