package com.abdul.weatherappcollabera.permissions

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.abdul.weatherappcollabera.tabview.CurrentWeatherFragment

class ApplicationClass : Application() {
    lateinit var activity: AppCompatActivity
    lateinit var fragment: CurrentWeatherFragment

    companion object {
        lateinit var mInstance: ApplicationClass

        private operator fun get(context: Context): ApplicationClass {
            return context.applicationContext as ApplicationClass
        }
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
    }
}