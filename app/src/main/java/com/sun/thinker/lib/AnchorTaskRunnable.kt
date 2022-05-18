package com.sun.thinker.lib

import android.os.Process
import android.os.SystemClock
import com.sun.thinker.lib.monitor.AnchorProject

class AnchorTaskRunnable(
    private val mAnchorProject: AnchorProject,
    private val mAnchorTask: AnchorTask
) : Runnable {

    override fun run() {
        Process.setThreadPriority(mAnchorTask.priority())
        //前置任务没有执行完毕的话，等待，执行完毕的话，往下走
        mAnchorTask.await()
        mAnchorTask.onStart()
        //执行任务
        var mStartTime = SystemClock.elapsedRealtime()
        mAnchorTask.run()
        val mExecuteTime = SystemClock.elapsedRealtime() - mStartTime
        mAnchorProject.record(mAnchorTask.getTaskName(), mExecuteTime)
        mAnchorTask.onFinish()
        //通知子任务，当前任务执行完毕了，相应的计数器要减一
        mAnchorProject.setNotifyChildren(mAnchorTask)
    }

}