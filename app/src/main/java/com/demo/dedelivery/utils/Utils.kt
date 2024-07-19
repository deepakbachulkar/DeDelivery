package com.demo.dedelivery.utils

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.demo.dedelivery.MainActivity
import com.demo.dedelivery.R
val OS_VERSION ="os_version"
val DEVICE_TOKEN ="device_token"

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "DO_DELIVERY_2"
        val descriptionText = "This is my channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("DO_DELIVERY_2", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

fun sendNotification(context: Context, message:String) {
    val builder = NotificationCompat.Builder(context, "DO_DELIVERY_2")
        .setSmallIcon(R.drawable.ic_notification) // Replace with your app's icon
        .setContentTitle("Location Update")
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(context)) {
        // notificationId is a unique int for each notification that you must define
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
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
        notify(1, builder.build())
    }


}

 fun createNotification(context: Context): Notification {
    val channelId = "DO_DELIVERY"
    val channelName = "DO DELIVERY"
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }
    val notificationIntent = Intent(context, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

    return NotificationCompat.Builder(context, channelId)
        .setContentTitle("Location Service")
        .setContentText("Running in background")
        .setContentIntent(pendingIntent)
        .build()
}

fun sharePrefSaveData(context: Context, flag:Boolean){
    val sharedPreferences = context.getSharedPreferences("DoDeliveryPref", MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean("online", flag)
    editor.apply()
}

fun sharePrefRetrievingData(context: Context):Boolean{
    val sharedPreferences = context.getSharedPreferences("DoDeliveryPref", MODE_PRIVATE)
    return sharedPreferences.getBoolean("online", false)
}

fun sharePrefSaveDataString(context: Context, key:String, value:String){
    val sharedPreferences = context.getSharedPreferences("DoDeliveryPref", MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString(key, value)
    editor.apply()
}

fun sharePrefRetrievingDataString(context: Context, key: String):String{
    val sharedPreferences = context.getSharedPreferences("DoDeliveryPref", MODE_PRIVATE)
    return sharedPreferences.getString(key, "")?:""
}

fun getOSVersion(): String {
    return "${Build.VERSION.RELEASE})"
}