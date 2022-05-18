package com.sun.thinker.demo

import android.app.Application
import android.content.Context
import com.sun.thinker.demo.anchor.TestTaskUtils
import com.sun.thinker.lib.OnProjectExecuteListener
import com.sun.thinker.lib.log.LogUtil
import com.sun.thinker.lib.monitor.OnGetMonitorRecordCallback

class ThinkerApp : Application() {

    companion object {
        const val TAG = "ThinkerApp"

        lateinit var mThinkerApp: ThinkerApp
            private set

        @JvmStatic
        fun getInstance(): Application {
            return mThinkerApp;
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        LogUtil.d(TAG, "attachBaseContext: ")
    }

    override fun onCreate() {
        super.onCreate()
        LogUtil.d(TAG, "onCreate: ")

        mThinkerApp = this

        TestTaskUtils.executeTask(
            this,
            mOnProjectExecuteListener = object : OnProjectExecuteListener {
                override fun onProjectStart() {
                    LogUtil.i(TAG, "onProjectStart")
                }

                override fun onTaskFinish(mTaskName: String) {
                    LogUtil.i(TAG, "onTaskFinish, taskName is $mTaskName")
                }

                override fun onProjectFinish() {
                    LogUtil.i(TAG, "onTaskFinish")
                }

            },
            mOnGetMonitorRecordCallback = object : OnGetMonitorRecordCallback {
                override fun onGetTaskExecuteRecord(result: Map<String?, Long?>?) {
                    LogUtil.i(TAG, "onGetTaskExecuteRecord, result is $result")
                }

                override fun onGetProjectExecuteTime(costTime: Long) {
                    LogUtil.i(TAG, "onGetProjectExecuteTime, costTime is $costTime")
                }

            })
    }
}