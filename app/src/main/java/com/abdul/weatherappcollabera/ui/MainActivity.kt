package com.abdul.weatherappcollabera.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.abdul.weatherappcollabera.R
import com.abdul.weatherappcollabera.databinding.ActivityMainBinding
import com.abdul.weatherappcollabera.permissions.ApplicationClass
import com.abdul.weatherappcollabera.tabview.CurrentWeatherFragment
import com.abdul.weatherappcollabera.tabview.WeatherListFragment
import com.abdul.weatherappcollabera.tabview.WeatherTabPagerAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        ApplicationClass.mInstance.activity = this
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setWeatherAdapter()
    }

    private fun setWeatherAdapter() {
        val adapter = WeatherTabPagerAdapter(supportFragmentManager)
        adapter.addFragment(CurrentWeatherFragment(), getString(R.string.current_weather))
        adapter.addFragment(WeatherListFragment(), getString(R.string.weather_list))
        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }
}