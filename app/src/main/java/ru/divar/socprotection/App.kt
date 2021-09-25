package ru.divar.socprotection

import android.app.Application
import android.os.StrictMode
import androidx.appcompat.app.AppCompatDelegate
import ru.divar.socprotection.data.PreferenceRepository
import java.util.*

class App : Application() {
    lateinit var preferenceRepository: PreferenceRepository

    override fun onCreate() {
        super.onCreate()

        preferenceRepository = PreferenceRepository(
            getSharedPreferences(
                PreferenceRepository.DEFAULT_PREFERENCES,
                MODE_PRIVATE
            )
        )

        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .permitAll()
                .build()
        )

        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .build()
        )

        Thread.setDefaultUncaughtExceptionHandler { paramThread, paramThrowable ->
            paramThrowable.printStackTrace()
        }

        AppCompatDelegate.setDefaultNightMode(preferenceRepository.nightMode)
    }
}