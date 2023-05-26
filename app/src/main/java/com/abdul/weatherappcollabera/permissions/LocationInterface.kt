package com.abdul.weatherappcollabera.permissions

interface  LocationInterface {
    abstract fun onLocationChange(latitude: Double, longitude: Double)
}