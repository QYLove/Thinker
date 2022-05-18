package com.sun.thinker.demo.anchor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sun.thinker.R
import com.sun.thinker.lib.OnProjectExecuteListener
import com.sun.thinker.lib.monitor.OnGetMonitorRecordCallback
import kotlinx.android.synthetic.main.activity_anchor_task_test.*

class AnchorTaskTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anchor_task_test)
        initAnchorTask()
    }

    private fun initAnchorTask() {
        val sb2 = StringBuilder()
        activity_anchor_task_test_text_tv.text = "正在执行中"
        val projectExecuteListener = object : OnProjectExecuteListener {
            val sb = StringBuffer()
            override fun onProjectStart() {
                if (sb.length > 0) {
                    sb.delete(0, sb.length)
                }
            }

            override fun onTaskFinish(mTaskName: String) {
                sb.append("task $mTaskName execute finish \n")
            }

            override fun onProjectFinish() {
                activity_anchor_task_test_text_tv.post {
                    activity_anchor_task_test_text_tv.setText(sb.toString())
                }
            }
        }

        val onGetMonitorRecordCallback = object : OnGetMonitorRecordCallback {
            override fun onGetTaskExecuteRecord(result: Map<String?, Long?>?) {
                result?.entries?.iterator()?.forEach {
                    sb2.append(it.key).append("执行耗时")
                        .append(it.value).append("毫秒\n")
                }
                activity_anchor_task_test_text2_tv.text = sb2.toString()
            }

            override fun onGetProjectExecuteTime(costTime: Long) {
                sb2.append("总共执行耗时")
                    .append(costTime).append("毫秒\n")
            }
        }

        activity_anchor_task_test_execute_btn.setOnClickListener {
            sb2.clear()
            activity_anchor_task_test_text2_tv.text = "正在执行中"
            try {
                TestTaskUtils.executeTask(
                    this, projectExecuteListener, onGetMonitorRecordCallback
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        activity_anchor_task_test_execute2_btn.setOnClickListener {
            sb2.clear()
            activity_anchor_task_test_text2_tv.text = "正在执行中2"
            try {
                TestTaskUtils.executeTask(
                    this, projectExecuteListener, onGetMonitorRecordCallback
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}