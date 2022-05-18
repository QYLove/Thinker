package com.sun.thinker.demo.viewStub

import android.view.View
import com.sun.thinker.R
import com.sun.thinker.lib.log.LogUtil

class ViewStubTaskTitle(decorView: View) : ViewStubTask(decorView) {

    override fun getViewStubId(): Int {
        return R.id.activity_view_stub_demo_title_vs
    }

    override fun onInflateFinish() {
        super.onInflateFinish()
        LogUtil.i(TAG, "onInflateFinish: ViewStubTaskTitle")
    }
}

class ViewStubTaskContent(decorView: View) : ViewStubTask(decorView) {

    override fun getViewStubId(): Int {
        return R.id.activity_view_stub_demo_content_vs
    }

    override fun onInflateFinish() {
        super.onInflateFinish()
        LogUtil.i(TAG, "onInflateFinish: ViewStubTaskContent")
    }
}

class ViewStubTaskBottom(decorView: View) : ViewStubTask(decorView) {

    override fun getViewStubId(): Int {
        return R.id.activity_view_stub_demo_bottom_vs
    }

    override fun onInflateFinish() {
        super.onInflateFinish()
        LogUtil.i(TAG, "onInflateFinish: ViewStubTaskBottom")
    }
}