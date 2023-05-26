package com.abdul.weatherappcollabera.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abdul.weatherappcollabera.data.models.WeatherInfoResponse
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ApiRepository<T> {

    var data: MutableLiveData<T>? = null
    var dataError: MutableLiveData<WeatherInfoResponse>? = null


    init {
        if (data == null)
            data = MutableLiveData()
        if (dataError == null)
            dataError = MutableLiveData()
    }

    fun callApi(call: Call<T>?): LiveData<T> {

        call?.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                try {
                    getResponse(response)
                    //val tempInKelvin: Int? = weatherResponse?.list?.get(0)?.main?.temp?.toInt()
                    Log.d("abdul", "Repository Response: " + response)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                data != null
                getInstance(data!!)
                call.cancel()
            }
        })
        return data!!

    }

    fun getInstance(data: LiveData<T>): LiveData<T> {
        return data
    }

    private fun getError(data: MutableLiveData<WeatherInfoResponse>): LiveData<WeatherInfoResponse> {
        return data
    }

    fun getResponse(response: Response<T>) {

        when (response.code()) {
            200 -> {
                data!!.value = response.body()
                Log.d("abdul", "Api Response: " + response.body())
            }

            400 -> {
                postError(response)
            }

            else -> {
                postError(response)
            }
        }
    }

    fun postError(response: Response<T>) {
        dataError?.value = errorResponse(response.errorBody())
        getError(dataError!!)
    }


    private fun errorResponse(responseBody: ResponseBody?): WeatherInfoResponse? {

        var apiStatusResponseModel: WeatherInfoResponse? = null
        val gson = Gson()
        val adapter: TypeAdapter<*> =
            gson.getAdapter<WeatherInfoResponse>(WeatherInfoResponse::class.java)
        try {
            if (responseBody != null)
                apiStatusResponseModel =
                    adapter.fromJson(responseBody.string()) as WeatherInfoResponse

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return apiStatusResponseModel
    }
}