package com.abdul.weatherappcollabera.data.api

import com.abdul.weatherappcollabera.data.models.ApiStatus
import com.abdul.weatherappcollabera.data.models.WeatherInfoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiInterface {

    @GET("weather")
    fun getWeatherByLocation(@Query("lat") latitude: String, @Query("lon") longitude: String):
            Call<WeatherInfoResponse>

    @GET("weather")
    fun getWeatherByCity(@Query("q") cityId: String):
            Call<WeatherInfoResponse>
}