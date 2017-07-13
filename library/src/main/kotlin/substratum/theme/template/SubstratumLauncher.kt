package substratum.theme.template

import android.app.Activity

/**
 * Configurable options when launching the intent
 * The defaults are presented here and they can be changed
 * under [launchSubstratum]
 *
 * [debugMode] should be set to your respective BuildConfig.DEBUG_MODE
 * Do not share apks build with debugging!
 *
 * Note that all of the inner variables are prefixed with substratum
 * It's to avoid any confusion with any outer variables through Kotlin's DSL
 */
class SubstratumConfigs(val debugMode: Boolean) {
    /**
     * Enable to print out your current signature key
     * Make sure NOT to release your app with this enabled
     */
    var substratumPrintProductionKey = false
    /**
     * //todo figure out what this does
     */
    var substratumLaunchMode = ""
    /**
     * Your apk production signature
     * If set, your key will be verified on launch
     * If you don't know your key, enable [substratumPrintProductionKey]
     * but make sure to disable it upon public release
     */
    var substratumApkProductionSignature = ""
    /**
     * Your google public license key
     * If set, your key will be verified on launch
     * This key can be found through the play store
     */
    var substratumBase64LicenseKey = ""
    /**
     * Ensure that the user has an internet connection before proceeding to the theme
     */
    var substratumEnforceInternetCheck = false
        set(value) {
            if (!debugMode) field = value
        }
    /**
     * Make sure that the app is installed through the play store
     */
    var substratumEnforceGooglePlayInstall = false
        set(value) {
            if (!debugMode) field = value
        }
    /**
     * Make sure that the app is installed through amazon
     */
    var substratumEnforceAmazonAppStoreInstall = false
        set(value) {
            if (!debugMode) field = value
        }
    /**
     * Verify that theme ready gapps are installed
     */
    var substratumThemeReadyGapps = false
    var substratumFilterCheck = false
    /**
     * Forbids theme to be used if any of [BLACKLISTED_APPLICATIONS] are installed
     */
    var substratumDisableBlacklistedApps = true
    /**
     * Generated boolean indicating if any component needs to be checked
     * by the privacy checker
     */
    internal val substratumShouldCheckPiracy: Boolean
        get() = substratumApkProductionSignature.isNotBlank()
                || substratumBase64LicenseKey.isNotBlank()
                || substratumEnforceGooglePlayInstall
                || substratumEnforceAmazonAppStoreInstall

}

fun Activity.launchSubstratum(debugMode: Boolean, configs: SubstratumConfigs.() -> Unit = {}) {
    val conf = SubstratumConfigs(debugMode).apply {
        configs()
        if (substratumPrintProductionKey && packageManager.getInstallerPackageName(packageName).isNotBlank()) {
            log("App is installed from a known source and is likely in production; make sure to disable substratumPrintProductionKey.")
            substratumPrintProductionKey = false
        }
    }
    conf.configs()

}