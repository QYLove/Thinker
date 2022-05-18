package com.sun.thinker.lib.log

import android.util.Log

object LogUtil {

    enum class LogLevel {
        NONE {
            override val value: Int
                get() = -1
        },
        ERROR {
            override val value: Int
                get() = 0
        },
        WARN {
            override val value: Int
                get() = 1
        },
        INFO {
            override val value: Int
                get() = 2
        },
        DEBUG {
            override val value: Int
                get() = 3
        };

        abstract val value: Int
    }

    private var TAG = "SAF_L"

    //日志的等级，可以进行配置，最好在Application中进行全局的配置
    var mLogLevel = LogLevel.INFO

    /**
     * 支持传入类作为tag
     */
    @JvmStatic
    fun init(mClass: Class<*>) {
        TAG = mClass.simpleName
    }

    /**
     * 支持传入自己的tag，可扩展性更好
     */
    @JvmStatic
    fun init(mTag: String) {
        TAG = mTag
    }

    @JvmStatic
    fun e(mTag: String, mMessage: String) {
        if (LogLevel.ERROR.value <= mLogLevel.value) {
            if (mMessage.isNotBlank()) {
                Log.e("$TAG$mTag", mMessage)
            }
        }
    }

    @JvmStatic
    fun w(mTag: String, mMessage: String) {
        if (LogLevel.WARN.value <= mLogLevel.value) {
            if (mMessage.isNotBlank()) {
                Log.w("$TAG$mTag", mMessage)
            }
        }
    }

    @JvmStatic
    fun i(mTag: String, mMessage: String) {
        if (LogLevel.INFO.value <= mLogLevel.value) {
            if (mMessage.isNotBlank()) {
                Log.i("$TAG$mTag", mMessage)
            }
        }
    }

    @JvmStatic
    fun d(mTag: String, mMessage: String) {
        if (LogLevel.DEBUG.value <= mLogLevel.value) {
            if (mMessage.isNotBlank()) {
                Log.d("$TAG$mTag", mMessage)
            }
        }
    }
}