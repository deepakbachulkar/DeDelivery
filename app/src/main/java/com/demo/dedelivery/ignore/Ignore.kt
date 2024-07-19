package com.demo.dedelivery.ignore

class Ignore {
    //        val loginViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
//        loginViewModel.apiCall()

//        // Periodic work request
//        val periodicWorkRequest = PeriodicWorkRequest.Builder(LogsWorker::class.java, 1, TimeUnit.MINUTES).build()
//        WorkManager.getInstance(this).enqueue(periodicWorkRequest)

//        // Work with constraints
//        val constraints = Constraints.Builder()
//            .setRequiredNetworkType(NetworkType.CONNECTED)
//            .setRequiresCharging(true)
//            .build()
//        val constrainedWorkRequest = PeriodicWorkRequest.Builder(LogsWorker::class.java, 1, TimeUnit.MINUTES)
//            .setConstraints(constraints)
//            .build()
//        WorkManager.getInstance(this).enqueue(constrainedWorkRequest)

//      stopService()
//        startService()

//        broadcastReceiver = ResponseBroadcastReceiver()
//        val intentFilter = IntentFilter()
//        intentFilter.addAction(BackgroundService.ACTION)
//        registerReceiver(broadcastReceiver, intentFilter)
//        scheduleAlarm();

//    fun scheduleAlarm(){
//        val toastIntent = Intent(applicationContext, ToastBroadcastReceiver::class.java)
//        val toastAlarmIntent = PendingIntent.getBroadcast(applicationContext, 0, toastIntent, PendingIntent.FLAG_IMMUTABLE)
//        val startTime = System.currentTimeMillis() //alarm starts immediately
//        val backupAlarmMgr = this.getSystemService(ALARM_SERVICE) as AlarmManager
//        backupAlarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, startTime, 30000, toastAlarmIntent) // alarm will repeat after every 15 minutes
//    }

//
//    <receiver android:name=".ignore.ResponseBroadcastReceiver"
//    android:exported="true"
//    android:process=":remote">
//    <intent-filter>
//    <action android:name="com.demo.dedelivery.Receivers.ResponseBroadcastReceiver"/>
//    </intent-filter>
//    </receiver>
//
//    <receiver android:name=".ignore.ToastBroadcastReceiver" android:process=":remote">
//    </receiver>
//    <service android:name=".ignore.BackgroundService" android:exported="false" />

}