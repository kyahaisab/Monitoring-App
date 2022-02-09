package com.example.notesapp.worker

import android.content.Context
import android.util.Log
import androidx.work.*
import com.example.notesapp.alarmManager.AlarmInfo
import java.util.*


class MyWorkerService(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    companion object {
        const val PARSE_CONSTANT = "parse_constant"

        fun startWorkManager(context: Context) {
            val infoToWorker =
                Data.Builder().putString(MyWorkerService.PARSE_CONSTANT, "Info from Main to worker")
                    .build()
            val workRequest = OneTimeWorkRequestBuilder<MyWorkerService>()
                .build()
            Log.e("SAGAR", "created workmanager")
            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }

    override fun doWork(): Result {
        val fromMainActivity = inputData.getString(PARSE_CONSTANT)
        val ramUsage = getUsedMemorySize().toString()
        val currTime = Calendar.getInstance().time.toString()
        val data = Data.Builder().putString(PARSE_CONSTANT, "$currTime $ramUsage").build()

        //Because of inserting Toast message thing are not working
        //   Toast.makeText(applicationContext, "in MyWorker class", Toast.LENGTH_LONG).show()
        Log.e("SAGAR", "Visited Worker $ramUsage      $currTime")
        AlarmInfo.setAlert(applicationContext)
        return Result.success(data)
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