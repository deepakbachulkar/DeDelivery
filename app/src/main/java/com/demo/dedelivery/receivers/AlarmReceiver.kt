package com.demo.dedelivery.receivers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.demo.dedelivery.bgServices.LocationUpdateForegroundService
import com.demo.dedelivery.utils.sharePrefRetrievingData

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val flg = sharePrefRetrievingData(context)
        Log.d("DoDelivery","Is Online: $flg")
        // Your background task code here
        if(sharePrefRetrievingData(context)) {
            val serviceIntent = Intent(context, LocationUpdateForegroundService::class.java)
            context.startService(serviceIntent)
            // Reschedule the alarm to repeat after 10 seconds
            scheduleAlarm(context)
        }
    }

    companion object {
        fun scheduleAlarm(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE)
            val triggerTime = System.currentTimeMillis() + 10000 // 10 seconds
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
        }
    }
}