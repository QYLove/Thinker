package com.sun.thinker.lib

import android.os.Handler
import android.os.Looper

object ThreadUtils {

    fun runOnUiThread(r: Runnable) {
        if (isMainThread) {
            r.run()
        } else {
            LazyHolder.mUiThreadHander.post(r)
        }
    }

    fun runOnUiThreadAtFront(r: Runnable) {
        if (isMainThread) {
            r.run()
        } else {
            LazyHolder.mUiThreadHander.postAtFrontOfQueue(r)
        }
    }

    fun runOnUiThread(r: Runnable?, delay: Long) {
        LazyHolder.mUiThreadHander.postDelayed(r!!, delay)
    }

    fun removeCallbacks(r: Runnable?) {
        LazyHolder.mUiThreadHander.removeCallbacks(r!!)
    }

    fun checkAtMainThread() {
        val e = RuntimeException("not main thread")
        LazyHolder.mUiThreadHander.postDelayed({ throw e }, 5000)
    }

    val isMainThread: Boolean
        get() = Looper.getMainLooper() == Looper.myLooper()

    private object LazyHolder {
        val mUiThreadHander = Handler(Looper.getMainLooper())
    }
}