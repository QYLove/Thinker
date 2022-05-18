package com.sun.thinker.lib.monitor

interface OnGetMonitorRecordCallback {

    /**
     * 获取task执行的耗时
     *
     * @param result task执行的耗时。
     *               key是task名称
     *               value是task执行耗时 单位是毫秒
     */
    fun onGetTaskExecuteRecord(result: Map<String?, Long?>?)

    /**
     * 获取整个Project执行耗时
     *
     * @param costTime 整个Project执行耗时
     */
    fun onGetProjectExecuteTime(costTime: Long)
}