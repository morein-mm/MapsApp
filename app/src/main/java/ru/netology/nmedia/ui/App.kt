package ru.netology.nmedia.ui

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import ru.netology.nmedia.BuildConfig

class App : Application() {
    override fun onCreate() {
        MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY)
        super.onCreate()
    }
}