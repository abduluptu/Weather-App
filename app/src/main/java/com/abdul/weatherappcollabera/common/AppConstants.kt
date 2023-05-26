package com.abdul.weatherappcollabera.common

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class AppConstants {

    companion object {
        const val TABLE_NAME = "weatherTable"
        const val ROOM_DB_NAME = "weather_database"
        const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        const val API_KEY = "38f6442ec91a39187503664ad14540c9"

        fun snackeBarMessage(activity: AppCompatActivity, msg: String) {

            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
        }

        fun Int.unixTimestampToTimeString(): String {
            try {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = this * 1000.toLong()

                val outputDateFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
                outputDateFormat.timeZone = TimeZone.getDefault()
                return outputDateFormat.format(calendar.time)

            } catch (e: Exception) {
                e.printStackTrace()
            }
            return this.toString()
        }

    }

    fun Double.kelvinToCelsius(): Int {
        return (this - 273.15).toInt()
    }
}