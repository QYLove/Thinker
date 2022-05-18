package com.sun.thinker.lib

interface OnProjectExecuteListener {

    /**
     * 当project开始执行时，调用该函数
     * 注意：该回调函数在Task所在线程中回调，注意线程安全
     */
    fun onProjectStart()

    /**
     * 当Project其中一个Task执行结束时，调用该函数
     * 注意：该回调函数在Task所在线程中回调，注意线程安全
     */
    fun onTaskFinish(mTaskName: String)

    /**
     * 当Project执行结束时，调用该函数
     * 注意：该回调函数在Task所在线程中回调，注意线程安全
     */
    fun onProjectFinish();
}