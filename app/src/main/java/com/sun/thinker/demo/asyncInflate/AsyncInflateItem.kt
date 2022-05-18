package com.sun.thinker.demo.asyncInflate

import android.view.View
import android.view.ViewGroup

class AsyncInflateItem constructor(
    var inflateKey: String,
    var layoutResId: Int,
    var parent: ViewGroup? = null,
    var callback: OnInflateFinishedCallBack? = null
) {
    var inflatedView: View? = null
    private var cancelled = false
    private var inflating = false

    fun isCancelled(): Boolean {
        synchronized(this) {
            return cancelled
        }
    }

    fun setCancelled(cancelled: Boolean) {
        synchronized(this) {
            this.cancelled = cancelled
        }
    }

    fun isInflating(): Boolean {
        synchronized(this) {
            return inflating
        }
    }

    fun setInflating(inflating: Boolean) {
        synchronized(this) {
            this.inflating = inflating
        }
    }

    override fun toString(): String {
        return "AsyncInflateItem(inflateKey='$inflateKey', layoutResId=$layoutResId, parent=$parent, callback=$callback, inflatedView=$inflatedView, cancelled=$cancelled, inflating=$inflating)"
    }


    interface OnInflateFinishedCallBack {
        fun onInflateFinished(item: AsyncInflateItem)
    }
}