package com.sun.thinker.demo.asyncInflate.page

import android.content.Context
import com.sun.thinker.R
import com.sun.thinker.demo.asyncInflate.AsyncInflateItem
import com.sun.thinker.demo.asyncInflate.AsyncInflateManager
import com.sun.thinker.demo.asyncInflate.LaunchInflateKey.LAUNCH_FRAGMENT_MAIN
import com.sun.thinker.demo.getSP

object AsyncUtils {

    fun asyncInflate(mContext:Context?){
        mContext ?: return
        val asyncInflateItem =
            AsyncInflateItem(
                LAUNCH_FRAGMENT_MAIN,
                R.layout.fragment_asny,
                null,
                null
            )
        AsyncInflateManager.instance.asyncInflate(mContext,asyncInflateItem)
    }

    fun isHomeFragmentOpen() =
        getSP("async_config").getBoolean("home_fragment_switch",true)
}