package com.abdul.weatherappcollabera.permissions

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionCheck (appCompatActivity: AppCompatActivity){

    private val appCompatActivity:AppCompatActivity

    init {
        this.appCompatActivity=appCompatActivity
    }

    companion object {
        const val MY_PERMISSIONS_REQUEST_LOCATION = 99
        const val MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION = 66
    }


    fun checkPermission(){
        if ((ContextCompat.checkSelfPermission(appCompatActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
                    ) && (ContextCompat.checkSelfPermission(
                appCompatActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
                    == PackageManager.PERMISSION_GRANTED)
        ) {

            Utils.displayLocationSettingsRequest(appCompatActivity, appCompatActivity)



        } else {
            checkLocationPermission()
        }
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                appCompatActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    appCompatActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {

                AlertDialog.Builder(appCompatActivity)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->

                        requestLocationPermission()
                    }
                    .create().show()
            } else {

                requestLocationPermission()
            }
        }
    }

    fun requestLocationPermission() {
        ActivityCompat.requestPermissions(appCompatActivity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            MY_PERMISSIONS_REQUEST_LOCATION
        )
    }
}