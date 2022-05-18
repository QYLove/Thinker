package com.sun.thinker.demo.viewStub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sun.thinker.R

class ViewStubDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_stub_demo)
        val decorView = this.window.decorView
        ViewStubTaskManager.instance(decorView)
            .addTask(ViewStubTaskTitle(decorView))
            .addTask(ViewStubTaskContent(decorView))
            .addTask(ViewStubTaskBottom(decorView))
            .start()
    }

}