package com.example.notesapp.worker

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log
import androidx.work.*
import com.example.notesapp.Note
import com.example.notesapp.NoteDataBase
import com.example.notesapp.NoteRepository
import com.example.notesapp.alarmManager.AlarmInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class MyWorkerService(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
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

    override fun doWork(): Result {
        val fromMainActivity = inputData.getString(PARSE_CONSTANT)
        val ramUsage = getUsedMemorySize().toString()
        val currTime = Calendar.getInstance().time.toString()
        val chargingStatus = isCharging()
        val data = Data.Builder().putString(PARSE_CONSTANT, "$currTime $ramUsage").build()

        //Because of inserting Toast message thing are not working
        //   Toast.makeText(applicationContext, "in MyWorker class", Toast.LENGTH_LONG).show()
        Log.e("SAGAR", "Visited Worker $ramUsage      $currTime")

        CoroutineScope(Dispatchers.Main).launch {
            repository.insert(Note("->TIME: $currTime\n->RAM RAM: $ramUsage\n->CHARGING STATE: $chargingStatus"))
        }

        AlarmInfo.setAlert(applicationContext)
        return Result.success(data)
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
}