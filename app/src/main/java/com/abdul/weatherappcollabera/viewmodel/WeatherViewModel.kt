package com.abdul.weatherappcollabera.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.abdul.weatherappcollabera.data.api.ApiInterface
import com.abdul.weatherappcollabera.data.api.RetrofitSingelton
import com.abdul.weatherappcollabera.data.models.WeatherInfoResponse
import com.abdul.weatherappcollabera.data.repository.ApiRepository

class WeatherViewModel : ViewModel() {

    private var apiRepository: ApiRepository<WeatherInfoResponse>? = null
    private var data: LiveData<WeatherInfoResponse>? = null
    private var apiRequest: ApiInterface = RetrofitSingelton.getInstance()!!
    private var dataError: LiveData<WeatherInfoResponse>? = null


    init {
        if (apiRepository == null)
            apiRepository = ApiRepository()
    }


    fun getWeatherbyCurrentLocationList(lat: String, lng: String) {
        data = apiRepository!!.callApi(apiRequest.getWeatherByLocation(lat, lng))
        Log.d("abdul", "ViewModel Response: ")
    }

    fun getWeatherByCity(city: String) {
        data = apiRepository!!.callApi(apiRequest.getWeatherByCity(city))
    }

    fun getResponseLiveData(): LiveData<WeatherInfoResponse>? {
        if (data != null)
            data = apiRepository?.data
        return data
    }

    fun getResponseError(): LiveData<WeatherInfoResponse>? {
        if (dataError == null)
            dataError = apiRepository?.dataError
        return dataError
    }
}