package com.sun.thinker.lib

import android.os.Process
import androidx.annotation.CallSuper
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.CountDownLatch

abstract class AnchorTask(
    private val mName: String
) : IAnchorTask {

    companion object {
        const val TAG = "AnchorTask"
    }

    private lateinit var mCountDownLatch: CountDownLatch

    private val mCopyOnWriteArrayList: CopyOnWriteArrayList<IAnchorCallBack> by lazy {
        CopyOnWriteArrayList<IAnchorCallBack>()
    }

    val mDependList: MutableList<String> = ArrayList()

    private fun getListSize() = getDependsTaskList()?.size ?: 0

    override fun getTaskName(): String {
        return mName
    }

    override fun priority(): Int {
        return Process.THREAD_PRIORITY_FOREGROUND
    }

    override fun needWait(): Boolean {
        return true
    }

    fun afterTask(mTaskName: String) {
        mDependList.add(mTaskName)
    }

    fun await() {
        tryToInitCountDown()
        mCountDownLatch.await()
    }

    @Synchronized
    private fun tryToInitCountDown() {
        if (!this::mCountDownLatch.isInitialized) {
            mCountDownLatch = CountDownLatch(mDependList.size)
        }
    }

    fun countdown() {
        tryToInitCountDown()
        mCountDownLatch.countDown()
    }

    override fun isRunOnMainThread(): Boolean {
        return false
    }

    fun getDependsTaskList(): List<String>? {
        return mDependList;
    }

    @CallSuper
    override fun onAdd() {
        mCopyOnWriteArrayList.forEach {
            it.onAdd()
        }
    }

    @CallSuper
    override fun onStart() {
        mCopyOnWriteArrayList.forEach {
            it.onStart()
        }
    }

    @CallSuper
    override fun onFinish() {
        mCopyOnWriteArrayList.forEach {
            it.onFinish()
        }
    }

    fun addCallback(mIAnchorCallBack: IAnchorCallBack?) {
        mIAnchorCallBack ?: return
        mCopyOnWriteArrayList.add(mIAnchorCallBack)
    }

    fun removeCallback(mIAnchorCallBack: IAnchorCallBack?) {
        mIAnchorCallBack ?: return
        mCopyOnWriteArrayList.remove(mIAnchorCallBack)
    }

    override fun toString(): String {
        return "AnchorTask(name='$mName',dependList is $mDependList)"
    }
}