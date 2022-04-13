package com.example.notesapp.worker

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.BatteryManager
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.work.*
import com.example.notesapp.Note
import com.example.notesapp.NoteDataBase
import com.example.notesapp.NoteRepository
import com.example.notesapp.alarmManager.AlarmInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.util.*


class MyWorkerService(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    val repository: NoteRepository

    companion object {
        const val PARSE_CONSTANT = "parse_constant"
        lateinit var workRequest: OneTimeWorkRequest

        fun startWorkManager(context: Context) {
            val infoToWorker =
                Data.Builder().putString(MyWorkerService.PARSE_CONSTANT, "Info from Main to worker")
                    .build()
            workRequest = OneTimeWorkRequestBuilder<MyWorkerService>()
                .build()
            Log.e("SAGAR", "created workmanager")
            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }

    init {
        val dao = NoteDataBase.getDatabase(context).getNoteDao()
        repository = NoteRepository(dao)
    }

    @SuppressLint("Range", "Recycle")
    override suspend fun doWork(): Result {
        return coroutineScope {
            withContext(Dispatchers.IO) {
                try {
                    val fromMainActivity = inputData.getString(PARSE_CONSTANT)
                    val ramUsage = getUsedMemorySize().toString()
                    val currTime = Calendar.getInstance().time.toString()
                    val chargingStatus = isCharging().toString()
                    val isScreenOn = checkScreenOn().toString()
                    val isBluetoothEnabled = isBluetoothEnabled()
                    val isInternetConnected = isNetworkConnected().toString()
                    val isLocationEnabled = isLocationEnabled(applicationContext)
                    val data =
                        Data.Builder().putString(PARSE_CONSTANT, "$currTime $ramUsage").build()

                    //Because of inserting Toast message thing are not working
                    //   Toast.makeText(applicationContext, "in MyWorker class", Toast.LENGTH_LONG).show()
                    Log.e("SAGAR", "Visited Worker $ramUsage      $currTime")

                    val newNote = Note()
                    newNote.name = currTime +
                            "\n\n->RAM RAM: $ramUsage" +
                            "\n\n->CHARGING STATE: $chargingStatus" +
                            "\n\n->SCREEN ONN: $isScreenOn" +
                            "\n\n->BLUETOOTH: $isBluetoothEnabled" +
                            "\n\n->INTERNET: $isInternetConnected" +
                            "\n\n->LOCATION: $isLocationEnabled"
                    newNote.ram = ramUsage
                    newNote.charging = chargingStatus
                    newNote.screen = isScreenOn
                    newNote.bluetooth = isBluetoothEnabled.toString()
                    newNote.internet = isInternetConnected
                    newNote.location = isLocationEnabled.toString()


                    //   CoroutineScope(Dispatchers.Main).launch {
                    repository.insert(newNote)
                    // Note:: if you want to insert as Note(....), then don't pass id , otherwise it will not be inserted
                    // }

//                    val cursor: Cursor? = applicationContext.contentResolver.query(
//                        Uri.parse("content://com.paytm.pos.provider/cardExceptions"),
//                        null,
//                        null,
//                        null,
//                        null
//                    )
//
//                    // iteration of the cursor
//                    // to print whole table
//
//                    // iteration of the cursor
//                    // to print whole table
//                    if (cursor != null) {
//                        if (cursor.moveToFirst()) {
//                            val strBuild = StringBuilder()
//                            while (!cursor.isAfterLast) {
//                                strBuild.append(
//                                    """${cursor.getString(cursor.getColumnIndex("time"))}-
//                                       ${cursor.getString(cursor.getColumnIndex("exceptionType"))}""".trimIndent()
//                                )
//                                cursor.moveToNext()
//                            }
//                            Log.e("SAGARI", strBuild.toString())
//                            newNote.name = strBuild.toString()
//                        } else {
//                            Log.e("SAGARI", "No data found")
//                        }
//                    }


                    AlarmInfo.setAlert(applicationContext)
                    Result.success(data)
                } catch (error: Throwable) {
                    Log.e("SAGAR", "This time worker failed")
                    Result.failure()
                }
            }
        }
    }

    private fun checkScreenOn(): Any? {
        val pm = applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager?
        val isScreenOnn = pm?.isInteractive
        return isScreenOnn
    }

    private fun isCharging(): Any {
        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            applicationContext.registerReceiver(null, ifilter)
        }
        val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val isCharging: Boolean = status == BatteryManager.BATTERY_STATUS_CHARGING
                || status == BatteryManager.BATTERY_STATUS_FULL

        val chargePlug: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) ?: -1
        val usbCharge: Boolean = chargePlug == BatteryManager.BATTERY_PLUGGED_USB
        val acCharge: Boolean = chargePlug == BatteryManager.BATTERY_PLUGGED_AC
        val ans = "Is Charging: $isCharging\n->USB POWER: $usbCharge\n->AC POWER: $acCharge"
        return ans
    }

    fun isBluetoothEnabled(): Boolean {
        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        return mBluetoothAdapter.isEnabled
    }

    private fun getUsedMemorySize(): Long {
        var freeSize = 0L
        var totalSize = 0L
        var usedSize = -1L
        try {
            val info = Runtime.getRuntime()
            freeSize = info.freeMemory()
            totalSize = info.totalMemory()
            usedSize = totalSize - freeSize
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return usedSize
    }

    private fun isNetworkConnected(): Boolean {
        val cm =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return cm!!.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }

    private fun isLocationEnabled(context: Context): Boolean? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // This is a new method provided in API 28
            val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            lm.isLocationEnabled
        } else {
            // This was deprecated in API 28
            val mode: Int = Settings.Secure.getInt(
                context.contentResolver, Settings.Secure.LOCATION_MODE,
                Settings.Secure.LOCATION_MODE_OFF
            )
            mode != Settings.Secure.LOCATION_MODE_OFF
        }
    }
}

//1.  not using Periodic work request because its min cycle is 15 min: https://developer.android.com/reference/androidx/work/PeriodicWorkRequest#MIN_PERIODIC_INTERVAL_MILLIS
// 2. Another way to acheieve the behaviour is to create OneTimeWorkRequest and that worker request, schedule another one for the interval you want with an initial delay
/*
class UploadWorker(
    @NonNull context: Context,
    @NonNull params: WorkerParameters?
) : Worker(context, params!!) {
    private val context: Context
    override fun doWork(): Result {
        Log.i("tracer:", "Worker executed")
        // Indicate whether the work finished successfully with the Result
        val mywork = OneTimeWorkRequest.Builder(UploadWorker::class.java)
            .setInitialDelay(5, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(context).enqueue(mywork)
        return Result.success()
    }

    init {
        this.context = context
    }
}
*/