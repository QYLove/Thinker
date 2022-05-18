package com.sun.thinker.demo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sun.thinker.R
import com.sun.thinker.demo.anchor.AnchorTaskTestActivity
import com.sun.thinker.demo.asyncInflate.AsyncActivity
import com.sun.thinker.demo.asyncInflate.page.AsyncUtils
import com.sun.thinker.demo.flow.FlowLayoutDemoActivity
import com.sun.thinker.demo.viewStub.ViewStubDemoActivity

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activity_main_stub_btn.setOnClickListener {
            startActivity(Intent(this@MainActivity, ViewStubDemoActivity::class.java))
        }

        activity_main_anchor_task_btn.setOnClickListener {
            startActivity(Intent(this@MainActivity, AnchorTaskTestActivity::class.java))
        }

        activity_main_flow_layout_demo_btn.setOnClickListener {
            startActivity(Intent(this@MainActivity, FlowLayoutDemoActivity::class.java))
        }

        activity_main_async_btn.setOnClickListener {
            startActivity(Intent(this@MainActivity, AsyncActivity::class.java))
        }

        val isOpen = AsyncUtils.isHomeFragmentOpen()
        updateText(isOpen)

        activity_main_async_switch.isChecked = isOpen

        activity_main_switch_ll.setOnClickListener {
            val b = !activity_main_async_switch.isChecked
            updateChecked(b)
        }

        activity_main_async_switch.setOnCheckedChangeListener() { buttonView, isChecked ->
            updateChecked(isChecked)
        }
    }

    private fun updateText(b: Boolean) {
        if (b) {
            activity_main_async_text_tv.setText("首页 Fragment 开启异步加载")
        } else {
            activity_main_async_text_tv.setText("首页 Fragment 关闭异步加载")
        }
    }

    private fun updateChecked(b: Boolean) {
        updateText(b)
        activity_main_async_switch.isChecked = b
        getSP("async_config").spApplyBoolean("home_fragment_switch", b)
    }
}