package com.sun.thinker.lib

open interface IAnchorTaskCreator {

    /**
     * 根据Task名称 创建Task实例
     * 这个接口需要使用者自己实现
     * 创建后的实例会被缓存起来
     */
    fun createTask(mTaskName: String): AnchorTask?

}

open class TaskCreatorWrap(var mIAnchorTaskCreator: IAnchorTaskCreator?) : IAnchorTaskCreator {

    private var mMap: MutableMap<String, AnchorTask?> = HashMap()

    override fun createTask(mTaskName: String): AnchorTask? {
        val mAnchorTask = mMap[mTaskName]
        mAnchorTask?.let {
            return it
        }
        return mIAnchorTaskCreator?.createTask(mTaskName)
    }

    fun checkTaskIsExist(mTaskName: String): Boolean {
        return mMap.containsKey(mTaskName)
    }

}