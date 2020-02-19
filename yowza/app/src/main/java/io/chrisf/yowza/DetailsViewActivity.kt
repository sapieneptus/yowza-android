package io.chrisf.yowza

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import net.hockeyapp.android.LoginManager
import net.hockeyapp.android.UpdateManager
import net.hockeyapp.android.utils.HockeyLog
import java.lang.RuntimeException

class DetailsViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_view)

        val versionString = "Version: " + BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")"
        val versionView = findViewById<TextView>(R.id.textViewVersion)
        versionView.text = versionString

        val config = Config.fetch(this)!!
        val authName = when(config.authType) {
            LoginManager.LOGIN_MODE_ANONYMOUS -> "Anonymous"
            LoginManager.LOGIN_MODE_VALIDATE -> "Validate"
            LoginManager.LOGIN_MODE_EMAIL_ONLY -> "Email Only"
            LoginManager.LOGIN_MODE_EMAIL_PASSWORD -> "Email Password"
            else -> throw RuntimeException("Unexpected auth type: ${config.authType}")
        }

        val configString = "Environment: ${config.environment}\nAuth Type: ${authName}"
        val configTextView = findViewById<TextView>(R.id.textViewConfig)
        configTextView.text = configString

        initializeSDK()
    }

    fun initializeSDK() {
        val config = Config.fetch(this)!!
        HockeyLog.setLogLevel(Log.VERBOSE)
        LoginManager.register(this, config.appId, config.appSecret, config.environment.url, config.authType, null)
        LoginManager.verifyLogin(this, intent)
        checkForUpdates()
    }

    private fun checkForUpdates() {
        val config = Config.fetch(this)!!
        UpdateManager.register(this, config.environment.url, config.appId, null)
    }

    private fun unregisterManagers() {
        UpdateManager.unregister()
    }

    public override fun onDestroy() {
        super.onDestroy()
        unregisterManagers()
    }
}
