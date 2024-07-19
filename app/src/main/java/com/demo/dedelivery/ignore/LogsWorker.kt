package com.demo.dedelivery.ignore

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationWorker(appContext: Context, params: WorkerParameters) : Worker(appContext, params) {

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(appContext)
    }

    override fun doWork(): Result {
        try {}catch (e: Exception) {
            Result.failure()
        }
        return Result.success()
    }
}
