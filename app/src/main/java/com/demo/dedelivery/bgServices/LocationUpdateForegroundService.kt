package com.demo.dedelivery.bgServices

import android.Manifest
import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import com.demo.dedelivery.networkcall.ApiCallServices
import com.demo.dedelivery.networkcall.RetrofitInstance
import com.demo.dedelivery.model.RequestLogs
import com.demo.dedelivery.utils.OS_VERSION
import com.demo.dedelivery.utils.createNotification
import com.demo.dedelivery.utils.sendNotification
import com.demo.dedelivery.utils.sharePrefRetrievingDataString
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService


class LocationUpdateForegroundService : Service() {
    private var scheduler: ScheduledExecutorService? = null
    var latitude:Double = 0.0
    var longitude:Double = 0.0
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var context:Context?=null


    override fun onCreate() {
        super.onCreate()
        context = this
        scheduler = Executors.newSingleThreadScheduledExecutor()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    // Process location data here
                    latitude = location.latitude
                    longitude = location.longitude
                    Log.d("DoDelivery","LAst Lat: $latitude  $longitude")
                    context?.let {
                        apiCall(context = it)
                    }
                }
            }
        }
        val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if(!isGpsEnabled && !isNetworkEnabled) {
            return
        }
         locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                result.locations.lastOrNull()?.let { location ->
                    latitude = location.latitude
                    longitude = location.longitude
                    Log.d("DoDelivery","Updated Lat: $latitude  $longitude")
//                    context?.let {
//                        apiCall(context = it)
//                    }
                }
            }

             override fun onLocationAvailability(p0: LocationAvailability) {
                 super.onLocationAvailability(p0)
             }

        }
    }

    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        context?.let {
            startForeground(1, createNotification(it))
        }
        getLastLocation(context = this)
        apiCall(this)
        val locationRequest = LocationRequest.create().apply {
            LocationManager.PASSIVE_PROVIDER
            interval = 10000 // 10 seconds
            fastestInterval = 5000 // 5 seconds
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return START_NOT_STICKY
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)

        return START_NOT_STICKY
    }

    fun apiCall(context: Context){
        Log.v("DoDelivery","Call API")
        val api = RetrofitInstance.getInstance().create(ApiCallServices::class.java)
        val jsonBody = RequestLogs()
        jsonBody.lat = latitude
        jsonBody.long = longitude
        jsonBody.os_version = sharePrefRetrievingDataString(context, OS_VERSION)

        val call = api.getLogs("eyJ0eXAiOiJqd3QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3MjA3NjkxNDQsImV4cCI6MTcyMzQ0NzU0NCwiaXNzIjoiY29kZWJyZXdJbm5vdmF0aW9uIn0.OvwtB8_kMSVNdJXf37dTLSx1QQVM5j5H3gmQU_n2Xy0", jsonBody)
        call.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                Log.v("DoDelivery","onResponse")
                var message = "Location fail to updated"
                if (response.isSuccessful && response.body()!=null){
                    if(response.code() == 200){
                        message = "Location successfully updated"
                        Log.v("DoDelivery","Success")
//                        Toast.makeText(applicationContext, "Success:200", Toast.LENGTH_SHORT).show()
                    }else {
                        Log.v("DoDelivery","Fail")
//                        Toast.makeText(applicationContext, "Fail", Toast.LENGTH_SHORT).show()
                    }
                }
                sendNotification(context, message)
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (scheduler != null) {
            scheduler?.shutdown()
        }
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun getLastLocation(context: Context) {
        if (ActivityCompat.checkSelfPermission( context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {}
        var fusedLocationClient: FusedLocationProviderClient? = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient?.lastLocation?.addOnSuccessListener {
            if (it != null) {
                    latitude = it.latitude
                    longitude = it.longitude
                Log.d("DoDelivery","Last Location Lat: $latitude  $longitude")
                    // Use the latitude and longitude as needed
                }
        }
    }

}
