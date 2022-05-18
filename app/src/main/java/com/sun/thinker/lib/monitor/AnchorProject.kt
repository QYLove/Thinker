package com.sun.thinker.lib.monitor

import android.content.Context
import com.sun.thinker.lib.*
import com.sun.thinker.lib.log.LogUtil
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class AnchorProject private constructor(mBuilder: Builder) {

    init {
        LogUtil.init("AnchorTaskLibrary")
    }

    private val mThreadPoolExecutor: ThreadPoolExecutor =
        mBuilder.mThreadPoolExecutor ?: TaskExecutorManager.mInstance.cpuThreadPoolExecutor

    //存储所有的任务 key是taskName value是AnchorTask
    private val mTaskMap: MutableMap<String, AnchorTask> = mBuilder.mTaskMap

    //存储当前人去的子任务  key是当前任务的taskName value是AnchorTask的list
    private val mTaskChildMap: MutableMap<String, ArrayList<AnchorTask>?> = mBuilder.mTaskChildMap

    //需要等待的任务总数，用于阻塞
    private val mCountDownLatch: CountDownLatch = mBuilder.mCountDownLatch

    //拓扑排序之后的主线程任务
    private val mMainList = mBuilder.mMainList

    //拓扑排序之后的子线程任务
    private val mThreadList = mBuilder.mThreadList

    private val mTotalTaskSize = mBuilder.mList.size
    private val mFinishTask = AtomicInteger(0)

    private val mListeners: CopyOnWriteArrayList<OnProjectExecuteListener> = CopyOnWriteArrayList()

    private val mIAnchorTaskCreator: IAnchorTaskCreator? = null
    private val mCacheTask: AnchorTask? = null
    private val mExecuteMonitor: ExecuteMonitor = ExecuteMonitor()

    var mOnGetMonitorRecordCallback: OnGetMonitorRecordCallback? = null

    fun addListener(mOnProjectExecuteListener: OnProjectExecuteListener) {
        mListeners.add(mOnProjectExecuteListener)
    }

    fun removeListener(mOnProjectExecuteListener: OnProjectExecuteListener) {
        mListeners.remove(mOnProjectExecuteListener)
    }

    fun record(mTaskName: String, mExecuteTime: Long) {
        mExecuteMonitor.record(mTaskName, mExecuteTime)
    }

    /**
     * 通知child countDown，当前的阻塞任务书也需要countDown
     */
    fun setNotifyChildren(mAnchorTask: AnchorTask) {
        mTaskChildMap[mAnchorTask.getTaskName()]?.forEach {
            mTaskMap[it.getTaskName()]?.countdown()
        }
        if (mAnchorTask.needWait()) {
            mCountDownLatch.countDown()
        }
        mListeners.forEach {
            it.onTaskFinish(mAnchorTask.getTaskName())
        }
        mFinishTask.incrementAndGet()

        if (mFinishTask.get() == mTotalTaskSize) {
            mExecuteMonitor.recordProjectFinish()
            ThreadUtils.runOnUiThread(Runnable {
                mOnGetMonitorRecordCallback?.onGetProjectExecuteTime(mExecuteMonitor.mProjectCostTime)
                mOnGetMonitorRecordCallback?.onGetTaskExecuteRecord(mExecuteMonitor.executeTimeMap)
            })

            mListeners.forEach {
                it.onProjectFinish()
            }
        }
    }

    fun start(): AnchorProject {
        mExecuteMonitor.recordProjectStart()
        this.mListeners.forEach {
            it.onProjectStart()
        }
        this.mThreadList.forEach {
            mThreadPoolExecutor.execute(AnchorTaskRunnable(this, mAnchorTask = it))
        }
        this.mMainList.forEach {
            AnchorTaskRunnable(this, mAnchorTask = it).run()
        }
        return this
    }

    fun await(mTimeOutMillion: Long = -1) {
        if (mTimeOutMillion > 0) {
            mCountDownLatch.await(mTimeOutMillion, TimeUnit.MILLISECONDS)
        } else {
            mCountDownLatch.await()
        }
    }

    class Builder constructor() {
        companion object {
            private const val TAG = "AnchorTaskDispatcher"
        }

        var mThreadPoolExecutor: ThreadPoolExecutor? = null
            private set

        private lateinit var mContext: Context
        private var mLogLevel: LogUtil.LogLevel = LogUtil.mLogLevel
        private var mTimeOutMillion: Long = -1

        //存储所有的任务
        val mList: MutableList<AnchorTask> = ArrayList()

        //存储所有的任务 key是taskName ，value是AnchorTask
        val mTaskMap: MutableMap<String, AnchorTask> = HashMap()

        //存储当前任务的子任务 key是当前任务的taskName ，value是AnchorTask的list
        val mTaskChildMap: MutableMap<String, ArrayList<AnchorTask>?> = HashMap()

        //拓扑排序之后的主线程任务
        val mMainList: MutableList<AnchorTask> = ArrayList()

        //拓扑排序之后的子线程任务
        val mThreadList: MutableList<AnchorTask> = ArrayList()

        //需要等待的任务总数，用于阻塞
        lateinit var mCountDownLatch: CountDownLatch

        //需要等待的任务总数，用于CountDownLatch
        private val mNeedWaitCount: AtomicInteger = AtomicInteger()

        private var mStartTime = -1L

        private val mListeners: CopyOnWriteArrayList<OnProjectExecuteListener> =
            CopyOnWriteArrayList()

        private var mIAnchorTaskCreator: IAnchorTaskCreator? = null

        private val mAnchorTaskWrapper: TaskCreatorWrap = TaskCreatorWrap(mIAnchorTaskCreator)

        private var mAnchorTask: AnchorTask? = null

        fun setAnchorTaskCreator(mIAnchorTaskCreator: IAnchorTaskCreator): Builder {
            this.mIAnchorTaskCreator = mIAnchorTaskCreator
            mAnchorTaskWrapper.mIAnchorTaskCreator = mIAnchorTaskCreator
            return this
        }

        fun setContext(mContext: Context): Builder {
            this.mContext = mContext
            return this
        }

        fun setLogLevel(mLogLevel: LogUtil.LogLevel): Builder {
            this.mLogLevel = mLogLevel
            LogUtil.mLogLevel = mLogLevel
            return this
        }

        fun setThreadPoolExecutor(mThreadPoolExecutor: ThreadPoolExecutor?): Builder {
            this.mThreadPoolExecutor = mThreadPoolExecutor
            return this
        }

        fun setTimeOutMillion(mTimeOutMillion: Long): Builder {
            this.mTimeOutMillion = mTimeOutMillion
            return this
        }

        fun addTask(mTaskName: String): Builder {
            val mCreateTask = mAnchorTaskWrapper.createTask(mTaskName)
            mCreateTask ?: let {
                throw AnchorTaskException("could not find anchorTask,taskName is $mTaskName")
            }
            return addTask(mAnchorTask = mCreateTask)
        }

        fun afterTask(vararg mTaskName: String): Builder {
            mAnchorTask ?: let {
                throw AnchorTaskException("should be call addTask first")
            }

            mTaskName.forEach { mTaskName ->
                val mCreateTask = mAnchorTaskWrapper.createTask(mTaskName)
                mCreateTask ?: let {
                    throw AnchorTaskException("could not find anchorTask,taskName is $mTaskName")
                }
                mCreateTask?.afterTask(mTaskName)
            }
            return this
        }

        fun afterTask(vararg mAnchorTasks: AnchorTask): Builder {
            mAnchorTask ?: let {
                throw AnchorTaskException("should be call addTask first")
            }
            mAnchorTasks.forEach {
                mAnchorTask?.afterTask(it.getTaskName())
            }
            return this
        }

        fun addTask(mAnchorTask: AnchorTask): Builder {
            this.mAnchorTask = mAnchorTask
            mList.add(mAnchorTask)
            mAnchorTask.onAdd()
            if (mAnchorTask.needWait()) {
                mNeedWaitCount.incrementAndGet()
            }
            return this
        }

        fun build(): AnchorProject {
            val mSortResult = AnchorTaskUtils.getSortResult(mList, mTaskMap, mTaskChildMap)
            LogUtil.d(TAG, "start: sortResult is $mSortResult")
            mSortResult.forEach {
                if (it.isRunOnMainThread()) {
                    mMainList.add(it)
                } else {
                    mThreadList.add(it)
                }
            }

            mCountDownLatch = CountDownLatch(mNeedWaitCount.get())
            return AnchorProject(this)
        }
    }
}