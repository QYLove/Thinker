package com.sun.thinker.lib.monitor

import android.os.Handler
import android.os.Looper
import java.util.concurrent.ConcurrentHashMap

/**
 * 监控Project执行性能的类
 * 会记录每一个Task执行时间 以及整个Project执行时间
 */
internal class ExecuteMonitor {

    private val mExecuteTimeMap: MutableMap<String?, Long?> = ConcurrentHashMap()
    private var mStartTime: Long = 0

    /**
     * Project执行时间
     */
    var mProjectCostTime: Long = 0
        private set
    private var mHandler: Handler? = null

    /**
     * 记录task执行时间
     * @param mTaskName taskName的名称
     * @param mExecuteTime 执行的时间
     */
    fun record(mTaskName: String, mExecuteTime: Long) {
        mExecuteTimeMap[mTaskName] = mExecuteTime
    }

    /**
     * 已执行完的每个task的执行时间
     */
    val executeTimeMap: Map<String?, Long?>
        get() = mExecuteTimeMap

    /**
     * 在Project结束时打点，记录耗时
     */
    fun recordProjectStart() {
        mStartTime = System.currentTimeMillis()
    }

    /**
     * 在Project结束时打点，记录耗时
     */
    fun recordProjectFinish() {
        mProjectCostTime = System.currentTimeMillis() - mStartTime
    }

    /**
     * 通过toast来告警
     * @param msg 告警内容
     * @param args format参数
     */
    private fun toastToWarn(msg: String, vararg args: Any) {
        handler.post {
            val formattedMsg: String
            formattedMsg = if (args == null) {
                msg
            } else {
                String.format(msg, *args)
            }
        }
    }

    private val handler: Handler
        private get() {
            if (mHandler == null) {
                mHandler = Handler(Looper.getMainLooper())
            }
            return mHandler!!
        }

}