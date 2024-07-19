package com.demo.dedelivery

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.demo.dedelivery.bgServices.LocationUpdateForegroundService
import com.demo.dedelivery.receivers.AlarmReceiver
import com.demo.dedelivery.utils.OS_VERSION
import com.demo.dedelivery.utils.createNotificationChannel
import com.demo.dedelivery.utils.getOSVersion
import com.demo.dedelivery.utils.sharePrefRetrievingData
import com.demo.dedelivery.utils.sharePrefSaveData
import com.demo.dedelivery.utils.sharePrefSaveDataString
import com.google.android.gms.location.FusedLocationProviderClient


class MainActivity : AppCompatActivity() {
    private var fusedLocationClient: FusedLocationProviderClient? = null
    val LOCATION_PERMISSION_REQUEST_CODE: Int = 1


    @SuppressLint("InvalidPeriodicWorkRequestInterval")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        findViewById<Switch>(R.id.switch1).isChecked = sharePrefRetrievingData(this)
        findViewById<Switch>(R.id.switch1).setOnCheckedChangeListener { compoundButton, flag ->
           if(flag){
//               startService(Intent(this, LocationService::class.java))

               startService()
           }else
               stopService()
        }
        createNotificationChannel(this)
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient!!.lastLocation
            .addOnSuccessListener(
                this
            ) { location ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    // Logic to handle location object
//                    val latitude = location.latitude
//                    val longitude = location.longitude
                    // Use the latitude and longitude as needed
                }
            }
    }

    override fun onResume() {
        super.onResume()
    }

    fun startService(){
        if (hasLocationPermission()) {
            // Start the foreground service
            val serviceIntent = Intent(this, LocationUpdateForegroundService::class.java)
            startService(serviceIntent)
            // Schedule the alarm to trigger the background task
            AlarmReceiver.scheduleAlarm(this)
            sharePrefSaveData(this, true)

        } else {
            requestLocationPermission()
        }
    }

//    fun preRequested(){
//        sharePrefSaveDataString(this, OS_VERSION, getOSVersion())
//    }

    fun stopService(){
        val serviceIntent = Intent(this, LocationUpdateForegroundService::class.java)
        stopService(serviceIntent)
//        AlarmReceiver.scheduleAlarm(this)
        sharePrefSaveData(this, false)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            } else {
                // Permission denied, handle appropriately
            }
        }
    }

    private fun hasLocationPermission(): Boolean {
        val fineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        val backgroundLocationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        } else {
            PackageManager.PERMISSION_GRANTED
        }
        return fineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                coarseLocationPermission == PackageManager.PERMISSION_GRANTED &&
                backgroundLocationPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
        ActivityCompat.requestPermissions(this, permissions.toTypedArray(), LOCATION_PERMISSION_REQUEST_CODE)
    }


}