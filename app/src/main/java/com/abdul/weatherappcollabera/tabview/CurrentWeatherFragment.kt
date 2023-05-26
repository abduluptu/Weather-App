package com.abdul.weatherappcollabera.tabview

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.abdul.weatherappcollabera.R
import com.abdul.weatherappcollabera.common.AppConstants
import com.abdul.weatherappcollabera.common.AppConstants.Companion.unixTimestampToTimeString
import com.abdul.weatherappcollabera.common.ProgressBar.getInstance
import com.abdul.weatherappcollabera.data.models.WeatherInfoResponse
import com.abdul.weatherappcollabera.permissions.ApplicationClass
import com.abdul.weatherappcollabera.permissions.LocationInterface
import com.abdul.weatherappcollabera.permissions.PermissionCheck
import com.abdul.weatherappcollabera.permissions.Utils
import com.abdul.weatherappcollabera.viewmodel.WeatherViewModel
import java.lang.String.format
import android.R.attr.name
import android.app.Activity
import android.net.ConnectivityManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.registerReceiver
import androidx.lifecycle.ViewModelProvider
import com.abdul.weatherappcollabera.common.AppConstants.Companion.snackeBarMessage


class CurrentWeatherFragment : Fragment(), LocationInterface {
    private lateinit var permissionCheck: PermissionCheck
    var homeViewModel: WeatherViewModel? = null

    lateinit var temprature_tv: TextView
    lateinit var city_tv: TextView
    lateinit var sunrise_tv: TextView
    lateinit var sunset_tv: TextView
    lateinit var child_linear: LinearLayout
    lateinit var search_iv: ImageView
    lateinit var city_et: EditText

    private lateinit var myView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationClass.mInstance.fragment = this
        homeViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        val someActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // Handle the result here
                    val data: Intent? = result.data
                    // Process the intent data
                    Utils.displayLocationSettingsRequest(
                        requireContext(),
                        ApplicationClass.mInstance.activity
                    )
                }
            }

        fun startSomeActivityForResult() {
            val intent = Intent(requireContext(), Utils::class.java)
            someActivityResultLauncher.launch(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_crrent_weather, container, false)

        temprature_tv = myView.findViewById(R.id.temprature_tv)
        city_tv = myView.findViewById(R.id.city_tv)
        sunrise_tv = myView.findViewById(R.id.sun_rise_tv)
        sunset_tv = myView.findViewById(R.id.sun_set_tv)
        child_linear = myView.findViewById(R.id.child_linear)
        search_iv = myView.findViewById(R.id.search_iv)
        city_et = myView.findViewById(R.id.city_et)
        callPermission()
        getInstance().progressBar(requireContext())
        click()
        return myView
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermissionCheck.MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        Utils.displayLocationSettingsRequest(
                            requireContext(),
                            ApplicationClass.mInstance.activity
                        )
                    } else {
                        permissionCheck.requestLocationPermission()
                    }

                } else if (!shouldShowRequestPermissionRationale(permissions[0])) {
                    startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts(
                                "package",
                                ApplicationClass.mInstance.activity.packageName,
                                null
                            ),
                        ),
                    )

                } else {
                    permissionCheck.requestLocationPermission()
                }
                return
            }

            PermissionCheck.MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION -> {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {

                        Toast.makeText(
                            requireContext(),
                            "Granted Background Location Permission",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {

                    Toast.makeText(requireContext(), "permission denied", Toast.LENGTH_LONG).show()
                }
                return

            }
        }
    }

    override fun onLocationChange(latitude: Double, longitude: Double) {
        if (isOnline(requireContext())) {
            homeViewModel?.getWeatherbyCurrentLocationList(
                latitude.toString(),
                longitude.toString()
            )
           /* Log.d("abdul", "Latitude: " + latitude)
            Log.d("abdul", "Longitude: " + longitude)*/
            getWeatherList()
        } else {
            Toast.makeText(requireContext(), getString(R.string.txt_not_connected), Toast.LENGTH_LONG,).show()
        }

    }

    fun callPermission() {

        Handler(Looper.getMainLooper()).postDelayed({
            PermissionCheck(ApplicationClass.mInstance.activity).checkPermission()
        }, 1000)
    }

    private fun getWeatherList() {
        homeViewModel?.getResponseLiveData()?.observe(
            this
        ) { response ->
            getInstance().progressBarState(false)
            if (response != null) {
                Log.d("abdul", "Response inFragment: " + response)
                child_linear.visibility = View.VISIBLE
                //snackeBarMessage(this!!, this!!.getString(R.string.weather_updated))
                setWeatherInfo(response)
            } else
                snackeBarMessage(
                    ApplicationClass.mInstance.activity!!,
                    this!!.getString(R.string.server_issue)
                )
        }

        homeViewModel?.getResponseError()?.observe(
            this
        ) { response ->
            getInstance().progressBarState(false)

            snackeBarMessage(ApplicationClass.mInstance.activity!!, response.message)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setWeatherInfo(weatherData: WeatherInfoResponse) {
        child_linear.visibility = View.VISIBLE
        val temp = format("%.2f", kelvintoCelcius(weatherData.main.temp))

        temprature_tv.text = "${temp}${getString(R.string.degree_celsius_symbol)}"
        city_tv.text =
            "${weatherData.name}${", "}${weatherData.sys.country}"
        sunrise_tv.text = weatherData.sys.sunrise.unixTimestampToTimeString()
        sunset_tv.text = weatherData.sys.sunset.unixTimestampToTimeString()
    }

    fun kelvintoCelcius(temp: Double): Double {
        return temp - 273.15F
    }

    fun click() {
        search_iv.setOnClickListener {
            val city = city_et.text.toString()
            if (city.length > 0) {
                closeKeyboard()
                getInstance().progressBar(requireContext())
                homeViewModel?.getWeatherByCity(city_et.text.toString())
            }
        }
    }

    fun closeKeyboard() {
        val imm = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(city_et.getWindowToken(), 0)

    }


    private val locationModeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == LocationManager.MODE_CHANGED_ACTION) {
                callPermission()
                getInstance().progressBar(requireContext())
                // Check if location services are enabled
                val locationManager =
                    requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val isLocationEnabled =
                    locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)


                if (!isLocationEnabled) {
                    Toast.makeText(requireContext(), "on location", Toast.LENGTH_SHORT).show()
                    // callPermission()
                    //getInstance().progressBar(this@HomeActivity)
                    // Location services have been disabled while the activity is running
                    // Your code here to handle this scenario
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Register the location mode receiver to receive broadcasts when the location mode changes
        requireContext().registerReceiver(
            locationModeReceiver,
            IntentFilter(LocationManager.MODE_CHANGED_ACTION)
        )
    }

    override fun onPause() {
        super.onPause()
        // Unregister the location mode receiver to avoid memory leaks
        requireContext().unregisterReceiver(locationModeReceiver)
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}