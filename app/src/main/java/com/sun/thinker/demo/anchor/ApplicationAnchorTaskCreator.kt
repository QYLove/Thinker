package com.sun.thinker.demo.anchor

import com.sun.thinker.lib.AnchorTask
import com.sun.thinker.lib.IAnchorTaskCreator
import com.sun.thinker.lib.log.LogUtil
import java.lang.Exception

const val TASK_NAME_ZERO = "zero"
const val TASK_NAME_ONE = "one"
const val TASK_NAME_TWO = "two"
const val TASK_NAME_THREE = "three"
const val TASK_NAME_FOUR = "four"
const val TASK_NAME_FIVE = "five"

class ApplicationAnchorTaskCreator : IAnchorTaskCreator {

    override fun createTask(mTaskName: String): AnchorTask? {
        when (mTaskName) {
            TASK_NAME_ZERO -> {
                return AnchorTaskZero()
            }
            TASK_NAME_ONE -> {
                return AnchorTaskOne()
            }
            TASK_NAME_TWO -> {
                return AnchorTaskTwo()
            }
            TASK_NAME_THREE -> {
                return AnchorTaskThree()
            }
            TASK_NAME_FOUR -> {
                return AnchorTaskFour()
            }
            TASK_NAME_FIVE -> {
                return AnchorTaskFive()
            }
        }
        return null
    }

}

class AnchorTaskZero : AnchorTask(TASK_NAME_ZERO) {

    override fun isRunOnMainThread(): Boolean {
        return false
    }

    override fun run() {
        val start = System.currentTimeMillis()
        try {
            Thread.sleep(300)
        } catch (e: Exception) {
        }
        LogUtil.i(TAG, "AnchorTaskZero: " + (System.currentTimeMillis() - start))
    }

}

class AnchorTaskOne : AnchorTask(TASK_NAME_ONE) {

    override fun isRunOnMainThread(): Boolean {
        return false
    }

    override fun run() {
        val start = System.currentTimeMillis()
        try {
            Thread.sleep(300)
        } catch (e: Exception) {
        }
        LogUtil.i(TAG, "AnchorTaskOne: " + (System.currentTimeMillis() - start))
    }

}

class AnchorTaskTwo : AnchorTask(TASK_NAME_TWO) {

    override fun isRunOnMainThread(): Boolean {
        return false
    }

    override fun run() {
        val start = System.currentTimeMillis()
        try {
            Thread.sleep(300)
        } catch (e: Exception) {
        }
        LogUtil.i(TAG, "AnchorTaskTwo: " + (System.currentTimeMillis() - start))
    }

}

class AnchorTaskThree : AnchorTask(TASK_NAME_THREE) {

    override fun isRunOnMainThread(): Boolean {
        return false
    }

    override fun run() {
        val start = System.currentTimeMillis()
        try {
            Thread.sleep(300)
        } catch (e: Exception) {
        }
        LogUtil.i(TAG, "AnchorTaskThree: " + (System.currentTimeMillis() - start))
    }

}

class AnchorTaskFour : AnchorTask(TASK_NAME_FOUR) {

    override fun isRunOnMainThread(): Boolean {
        return false
    }

    override fun run() {
        val start = System.currentTimeMillis()
        try {
            Thread.sleep(300)
        } catch (e: Exception) {
        }
        LogUtil.i(TAG, "AnchorTaskFour: " + (System.currentTimeMillis() - start))
    }

}

class AnchorTaskFive : AnchorTask(TASK_NAME_FIVE) {

    override fun isRunOnMainThread(): Boolean {
        return false
    }

    override fun run() {
        val start = System.currentTimeMillis()
        try {
            Thread.sleep(300)
        } catch (e: Exception) {
        }
        LogUtil.i(TAG, "AnchorTaskFive: " + (System.currentTimeMillis() - start))
    }

}