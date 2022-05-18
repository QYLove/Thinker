package com.sun.thinker.lib

import android.os.Process
import androidx.annotation.IntRange

interface IAnchorTask : IAnchorCallBack {

    /**
     * 获取任务昵称
     */
    fun getTaskName(): String

    /**
     * 是否在主线程执行
     */
    fun isRunOnMainThread(): Boolean

    /**
     * 任务优先级别
     */
    @IntRange(
        from = Process.THREAD_PRIORITY_FOREGROUND.toLong(),
        to = Process.THREAD_PRIORITY_LOWEST.toLong()
    )

    fun priority(): Int

    /**
     * 调用await方法 是否需要等待改任务执行完成
     *
     * true：不需要
     * false：需要
     */
    fun needWait(): Boolean

    /**
     * 任务被执行的时候回调
     */
    fun run()
}