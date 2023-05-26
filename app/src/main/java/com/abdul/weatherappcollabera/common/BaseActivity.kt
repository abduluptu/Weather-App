package com.abdul.weatherappcollabera.common

import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        supportActionBar?.hide()
    }
}