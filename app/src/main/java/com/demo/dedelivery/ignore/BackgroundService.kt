package com.demo.dedelivery.ignore

import android.app.IntentService
import android.content.Intent
import android.util.Log
import androidx.annotation.Nullable


@Suppress("DEPRECATION")
class BackgroundService  // Must create a default constructor
    : IntentService("backgroundService") {
    override fun onHandleIntent(@Nullable intent: Intent?) {
        // This describes what will happen when service is triggered
        Log.i("DeDelivery","Call Service running");
        //create a broadcast to send the toast message
        val toastIntent = Intent(ACTION)
        toastIntent.putExtra("toastMessage", "I'm running after ever 15 minutes")
        sendBroadcast(toastIntent)
    }

    companion object {
        const val ACTION: String =
            "ke.co.appslab.androidbackgroundservices.Receivers.ResponseBroadcastReceiver"
    }
}