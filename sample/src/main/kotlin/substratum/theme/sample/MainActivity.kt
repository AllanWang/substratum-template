package substratum.theme.sample

import android.app.Activity
import android.os.Bundle
import substratum.theme.template.launchSubstratum

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        launchSubstratum(BuildConfig.DEBUG) {
            substratumEnforceGooglePlayInstall = true
            substratumFilterCheck = true
        }

    }
}
