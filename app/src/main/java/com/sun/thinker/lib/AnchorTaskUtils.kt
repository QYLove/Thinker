package com.sun.thinker.lib

import com.sun.thinker.lib.log.LogUtil
import java.util.ArrayDeque

object AnchorTaskUtils {

    @JvmStatic
    fun getSortResult(
        mList: MutableList<AnchorTask>, mTaskMap: MutableMap<String, AnchorTask>,
        mTaskChildMap: MutableMap<String, ArrayList<AnchorTask>?>
    ): MutableList<AnchorTask> {
        val mResult = ArrayList<AnchorTask>()
        //入度为0的队列
        val mQueue = ArrayDeque<AnchorTask>()
        val mTaskIntegerHashMap = HashMap<String, Int>()

        //建立每个task的入度关系
        mList.forEach { mAnchorTask: AnchorTask ->
            val mTaskName = mAnchorTask.getTaskName()
            if (mTaskIntegerHashMap.containsKey(mTaskName)) {
                throw AnchorTaskException("anchorTask is repeat,anchorTask is $mAnchorTask,list is $mList")
            }

            val size = mAnchorTask.getDependsTaskList()?.size ?: 0

            mTaskIntegerHashMap[mTaskName] = size
            mTaskMap[mTaskName] = mAnchorTask
            if (size == 0) {
                mQueue.offer(mAnchorTask)
            }
        }

        //建立每个task的children 关系
        mList.forEach { mAnchorTask: AnchorTask ->
            mAnchorTask.getDependsTaskList()?.forEach { mTaskName: String ->
                var mList = mTaskChildMap[mTaskName]
                if (mList == null) {
                    mList = ArrayList()
                }
                mList.add(mAnchorTask)
                mTaskChildMap[mTaskName] = mList
            }
        }

        mTaskChildMap.entries.iterator().forEach {
            LogUtil.d("TAG", "key is ${it.key},value is ${it.value}")
        }

        //使用BFS方法获得邮箱无环图的拓扑排序
        while (!mQueue.isEmpty()) {
            val mAnchorTask = mQueue.pop()
            mResult.add(mAnchorTask)
            var mTaskName = mAnchorTask.getTaskName()
            mTaskChildMap[mTaskName]?.forEach {
                //遍历所有依赖这个顶点的顶点，移除该项顶点之后，如果入度为0 加入到该队列当中
                val mKey = it.getTaskName()
                var mResult = mTaskIntegerHashMap[mKey] ?: 0
                mResult--
                if (mResult == 0) {
                    mQueue.offer(it)
                }
                mTaskIntegerHashMap[mKey] = mResult
            }
        }

        //size不相等，证明又环
        if (mList.size != mResult.size) {
            throw AnchorTaskException("Ring appeared，Please Check.list is $mList, result is $mResult")
        }

        return mResult
    }
}