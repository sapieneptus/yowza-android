package io.chrisf.yowza

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import net.hockeyapp.android.LoginManager
import java.lang.RuntimeException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val versionString = "Version: " + BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")"
        val versionView = findViewById<TextView>(R.id.textViewVersion)
        versionView.text = versionString

        if (Config.exists(this)) showDetailsView()
    }

    fun goToDetailsView(view: View) {
        val authTypeGroup = findViewById<RadioGroup>(R.id.authTypeRadioGroup)
        val envGroup = findViewById<RadioGroup>(R.id.environmentRadioGroup)

        val authType = when(authTypeGroup.checkedRadioButtonId) {
            R.id.radioButtonAuthTypeEmailOnly -> LoginManager.LOGIN_MODE_EMAIL_ONLY
            R.id.radioButtonAuthTypeEmailPassword -> LoginManager.LOGIN_MODE_EMAIL_PASSWORD
            R.id.radioButtonAuthTypeValidate -> LoginManager.LOGIN_MODE_VALIDATE
            R.id.radioButtonAuthTypeAnonymous -> LoginManager.LOGIN_MODE_ANONYMOUS
            else -> -1
        }

        val env = when(envGroup.checkedRadioButtonId) {
            R.id.environmentRadioButtonRink -> Config.Environment.Rink
            R.id.environmentRadioButtonTraining -> Config.Environment.Training
            else -> null
        }

        if (env != null && authType != -1) {
            Config.save(this, env, authType)



            showDetailsView()
        }
    }

    fun showDetailsView() {
        val intent = Intent(this, DetailsViewActivity::class.java)
        startActivity(intent)
    }
}
