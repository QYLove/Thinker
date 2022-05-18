package com.sun.thinker.demo.asyncInflate

import android.content.Context
import android.content.MutableContextWrapper
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import com.sun.thinker.lib.ThreadUtils
import com.sun.thinker.lib.log.LogUtil
import java.lang.RuntimeException
import java.util.concurrent.*

class AsyncInflateManager private constructor() {

    //保存inflateKey以及InflateItem，里面包含所有要进行的inflate的任务
    private val mInflateMap:
            ConcurrentHashMap<String, AsyncInflateItem?> = ConcurrentHashMap()

    private val mInflateLatchMap:
            ConcurrentHashMap<String, CountDownLatch> = ConcurrentHashMap()

    companion object {
        private const val TAG = "AsyncInflateManager"
        private val cpuCount = Runtime.getRuntime().availableProcessors()
        val threadPool = ThreadPoolExecutor(
            cpuCount,
            cpuCount * 2,
            5,
            TimeUnit.SECONDS,
            LinkedBlockingDeque()
        ).apply {
            allowCoreThreadTimeOut(true)
        }

        @JvmStatic
        val instance by lazy {
            AsyncInflateManager()
        }

        /**
         * 空方法，为了可以提前加载AsyncInflateManager,并初始化 mThreadPool
         */
        fun init() {}
    }

    /**
     * 用来获得异步inflate出来的view
     *
     * @param context
     * @param layoutResId 需要拿的layoutId
     * @param parent container
     * @param inflateKey 每一个View会对应一个inflateKey，因为可能许多地方用的同一个layout，
     *                   但是需要inflate多个，用Inflate进行区分
     *
     * @param inflater 外部传进来的inflate，外面如果有inflater，传进来，用来进行可能的SyncInflate
     *
     * @return 最后inflate出来的view
     */
    @UiThread
    fun getInflatedView(
        context: Context?,
        layoutResId: Int,
        parent: ViewGroup?,
        inflateKey: String?,
        inflater: LayoutInflater
    ): View {
        if (!TextUtils.isEmpty(inflateKey) && mInflateMap.containsKey(inflateKey)) {
            val item = mInflateMap[inflateKey]
            val latch = mInflateLatchMap[inflateKey]
            if (item != null) {
                val resultView = item.inflatedView
                if (resultView != null) {
                    //拿到了view直接返回
                    removeInflateKey(item)
                    replaceContextForView(resultView, context)
                    LogUtil.i(TAG, "getInflatedView from cache: inflateKey is $inflateKey")
                    return resultView
                }

                if (item.isInflating() && latch != null) {
                    //没拿到view，但是在inflate中，等待返回
                    try {
                        latch.await()
                    } catch (e: InterruptedException) {
                        LogUtil.e(
                            TAG,
                            "getInflatedView from OtherThread: inflateKey is $inflateKey"
                        )
                    }

                    val inflatedView = item.inflatedView
                    if (inflatedView != null) {
                        removeInflateKey(item)
                        LogUtil.i(
                            TAG,
                            "getInflatedView from OtherThread: inflateKey is $inflateKey"
                        )
                        replaceContextForView(inflatedView, context)
                        return inflatedView
                    }
                }

                //如果还没开始inflate，则设置为false，UI线程进行inflate
                item.setCancelled(true)
            }
        }

        LogUtil.i(TAG, "getInflatedView from UI: inflateKey is $inflateKey")
        //拿异步inflate的view失败，UI线程inflate
        return inflater.inflate(layoutResId, parent, false)
    }

    private fun removeInflateKey(item: AsyncInflateItem) {}

    /**
     * 如果 inflater初始化时是传进来的application，inflate出来的view的context没法用来startActivity，
     * 因此用MutableContextWrapper进行包装，后续进行替换
     */
    private fun replaceContextForView(
        inflatedView: View?,
        context: Context?
    ) {
        if (inflatedView == null || context == null) {
            return
        }
        val cxt = inflatedView.context
        if (cxt is MutableContextWrapper) {
            cxt.baseContext = context
        }
    }

    fun asyncInflate(
        context: Context,
        vararg items: AsyncInflateItem?
    ) {
        items.forEach { item ->
            if (item == null || item.layoutResId == 0
                || mInflateMap.containsKey(item.inflateKey)
            ) {
                return
            }
            mInflateMap[item.inflateKey] = item
            onAsyncInflateReady(item)
            inflateWithThreadPool(context, item)
        }
    }

    fun remove(inflateKey: String){
        mInflateMap.remove(inflateKey)
        mInflateLatchMap.remove(inflateKey)
    }

    private fun onAsyncInflateReady(item: AsyncInflateItem) {}

    private fun onAsyncInflateStart(item: AsyncInflateItem) {}

    private fun onAsyncInflateEnd(item: AsyncInflateItem,success: Boolean){
        item.setInflating(false)
        val latch = mInflateLatchMap[item.inflateKey]
        latch?.countDown()
        if (success && item.callback != null){
            removeInflateKey(item)

            if (item.isCancelled()){//已经取消了，不再回调
                return
            }

            ThreadUtils.runOnUiThread{
                item.callback?.onInflateFinished(item)
            }
        }
    }

    private fun inflateWithThreadPool(
        context: Context,
        item: AsyncInflateItem
    ) {
        threadPool.execute {
            if (!item.isInflating() && !item.isCancelled()) {
                try {
                    onAsyncInflateStart(item)
                    item.setInflating(true)
                    mInflateLatchMap[item.inflateKey] = CountDownLatch(1)
                    val currentTimeMillis = System.currentTimeMillis()
                    item.inflatedView = BasicInflater(context).inflate(item.layoutResId,item.parent,false)
                    onAsyncInflateEnd(item,true)
                    val l = System.currentTimeMillis() - currentTimeMillis
                    LogUtil.i(TAG,"inflateWithThreadPool: inflateKey is ${item.inflateKey}, time is ${l}")
                }catch (e: RuntimeException){
                    LogUtil.e(TAG,"Failed to inflate resource in the background! Retrying on the UI thread")
                    onAsyncInflateEnd(item,false)
                }
            }
        }
    }

    private class BasicInflater(context: Context?) : LayoutInflater(context) {

        override fun cloneInContext(newContext: Context?): LayoutInflater {
            return BasicInflater(newContext)
        }

        @Throws(ClassNotFoundException::class)
        override fun onCreateView(name: String?, attrs: AttributeSet?): View {
            for (prefix in sClassPrefixList) {
                try {
                    val view = this.createView(name, prefix, attrs)
                    if (view != null) {
                        return view
                    }
                } catch (ignored: ClassNotFoundException) {
                }
            }
            return super.onCreateView(name, attrs)
        }

        companion object {
            private val sClassPrefixList =
                arrayOf("android.widget.", "android.webkit.", "android.app.")
        }
    }
}