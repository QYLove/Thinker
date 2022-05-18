package com.sun.thinker.demo.anchor

import android.content.Context
import com.sun.thinker.demo.ThinkerApp
import com.sun.thinker.lib.OnProjectExecuteListener
import com.sun.thinker.lib.TaskExecutorManager
import com.sun.thinker.lib.log.LogUtil
import com.sun.thinker.lib.monitor.AnchorProject
import com.sun.thinker.lib.monitor.OnGetMonitorRecordCallback

object TestTaskUtils {

    fun executeTask(
        mContext: Context,
        mOnProjectExecuteListener: OnProjectExecuteListener? = null,
        mOnGetMonitorRecordCallback: OnGetMonitorRecordCallback? = null
    ) {
        val mProject = AnchorProject.Builder()
            .setContext(mContext)
            .setLogLevel(LogUtil.LogLevel.DEBUG)
            .setAnchorTaskCreator(ApplicationAnchorTaskCreator())
            .addTask(TASK_NAME_ZERO)
            .addTask(TASK_NAME_ONE)
            .addTask(TASK_NAME_TWO)
            .addTask(TASK_NAME_THREE).afterTask(TASK_NAME_ZERO, TASK_NAME_ONE)
            .addTask(TASK_NAME_FOUR).afterTask(TASK_NAME_ONE, TASK_NAME_TWO)
            .addTask(TASK_NAME_FIVE).afterTask(TASK_NAME_THREE, TASK_NAME_FOUR)
            .setThreadPoolExecutor(TaskExecutorManager.mInstance.cpuThreadPoolExecutor)
            .build()
        mProject.addListener(object : OnProjectExecuteListener {
            override fun onProjectStart() {
                LogUtil.i(ThinkerApp.TAG, "onProjectStart")
            }

            override fun onTaskFinish(mTaskName: String) {
                LogUtil.i(ThinkerApp.TAG, "onTaskFinish, taskName is $mTaskName")
            }

            override fun onProjectFinish() {
                LogUtil.i(ThinkerApp.TAG, "onProjectFinish")
            }
        })

        mOnProjectExecuteListener?.let {
            mProject.addListener(it)
        }

        mProject.mOnGetMonitorRecordCallback = object : OnGetMonitorRecordCallback {
            override fun onGetTaskExecuteRecord(result: Map<String?, Long?>?) {
                mOnGetMonitorRecordCallback?.onGetTaskExecuteRecord(result)
            }

            override fun onGetProjectExecuteTime(costTime: Long) {
                mOnGetMonitorRecordCallback?.onGetProjectExecuteTime(costTime)
            }
        }

        mProject.start().await(1000)
    }

    fun executeTask2(
        mContext: Context,
        mOnProjectExecuteListener: OnProjectExecuteListener? = null,
        mOnGetMonitorRecordCallback: OnGetMonitorRecordCallback? = null
    ) {
        val mZero = AnchorTaskZero()
        val mOne = AnchorTaskOne()
        val mTwo = AnchorTaskTwo()
        val mThree = AnchorTaskThree()
        val mFour = AnchorTaskFour()
        val mFive = AnchorTaskFive()
        val mProject = AnchorProject.Builder()
            .setContext(mContext)
            .setLogLevel(LogUtil.LogLevel.DEBUG)
            .setAnchorTaskCreator(ApplicationAnchorTaskCreator())
            .addTask(mZero)
            .addTask(mOne)
            .addTask(mTwo)
            .addTask(mThree).afterTask(mZero, mOne)
            .addTask(mFour).afterTask(mOne, mTwo)
            .addTask(mFive).afterTask(mThree, mFour)
            .setThreadPoolExecutor(TaskExecutorManager.mInstance.cpuThreadPoolExecutor)
            .build()
        mProject.addListener(object : OnProjectExecuteListener {
            override fun onProjectStart() {
                LogUtil.i(ThinkerApp.TAG, "onProjectStart")
            }

            override fun onTaskFinish(mTaskName: String) {
                LogUtil.i(ThinkerApp.TAG, "onTaskFinish, taskName is $mTaskName")
            }

            override fun onProjectFinish() {
                LogUtil.i(ThinkerApp.TAG, "onProjectFinish")
            }
        })
        mOnProjectExecuteListener?.let {
            mProject.addListener(it)
        }
        mProject.mOnGetMonitorRecordCallback = object : OnGetMonitorRecordCallback {
            override fun onGetTaskExecuteRecord(result: Map<String?, Long?>?) {
                mOnGetMonitorRecordCallback?.onGetTaskExecuteRecord(result)
            }

            override fun onGetProjectExecuteTime(costTime: Long) {
                mOnGetMonitorRecordCallback?.onGetProjectExecuteTime(costTime)
            }
        }
        mProject.start().await(1000)
    }
}