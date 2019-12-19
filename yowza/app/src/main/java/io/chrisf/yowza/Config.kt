package io.chrisf.yowza

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class Config constructor(authType: Int, env: Environment) {
    var authType: Int = authType
    var environment: Environment = env
    var appId: String = if (this.environment == Environment.Training) "e3377c1b652e4d55b883fa43508c01c5" else "52ff6de42c284077ad2ce77558abb46b"
    var appSecret: String = if (this.environment == Environment.Training) "e5b76e53d83d2d76e4ba3d6ef315abfd" else "64d11682c0f579637f248f29ce279894"

    enum class Environment(val url: String) {
        Training("https://training.hockeyapp.net/"),
        Rink("https://rink.hockeyapp.net/");

        companion object {
            private val mapping = values().associateBy(Environment::url)
            fun fromString(value: String) = mapping[value] ?: error("Invalid value $value")
        }
    }

    companion object {
        const val PREFS_NAME = "yowzaPrefs"
        fun save(context: Context, environment: Environment, authType: Int) {
            val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString("environment", environment.url)
            editor.putInt("authType", authType)
            editor.apply()
        }

        fun fetch(context: Context): Config? {
            val prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            val envString = prefs.getString("environment", null)
            val authType = prefs.getInt("authType", -1)

            return if (envString != null && authType != -1) {
                val env = Environment.fromString(envString)
                Config(authType, env)
            } else {
                null
            }
        }

        fun exists(context: Context): Boolean {
            return fetch(context) != null
        }
    }
}