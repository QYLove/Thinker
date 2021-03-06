package com.sun.thinker.demo.viewStub

import android.view.View
import androidx.core.view.ViewCompat
import java.util.concurrent.CopyOnWriteArrayList

class ViewStubTaskManager private constructor(val decorView:View) : Runnable{

    private var iViewStubTask: IViewStubTask? = null

    companion object{

        const val TAG = "ViewStubTaskManager"

        @JvmStatic
        fun instance(decorView: View):ViewStubTaskManager{
            return ViewStubTaskManager(decorView)
        }
    }

    private val queue: MutableList<ViewStubTask> = CopyOnWriteArrayList()
    private var list: MutableList<ViewStubTask> = CopyOnWriteArrayList()

    fun setCallBack(iViewStubTask: IViewStubTask?): ViewStubTaskManager{
        this.iViewStubTask = iViewStubTask
        return this
    }

    fun addTask(viewStubTasks: List<ViewStubTask>): ViewStubTaskManager{
        queue.addAll(viewStubTasks)
        list.addAll(viewStubTasks)
        return this
    }

    fun addTask(viewStubTask: ViewStubTask):ViewStubTaskManager{
        queue.add(viewStubTask)
        list.add(viewStubTask)
        return this
    }

    fun start(){
        if (isEmpaty()){
           return
        }
        iViewStubTask?.beforeTaskExecute()
        //指定 decorView 绘制下一帧的时候回调用里面的 runnable
        ViewCompat.postOnAnimation(decorView,this)
    }

    fun stop(){
        queue.clear()
        list.clear()
        decorView.removeCallbacks(null)
    }

    private fun isEmpaty() = queue.isEmpty() || queue.size == 0

    override fun run() {
        if (isEmpaty()){
            //当队列不为空的时候，先加载当前 viewStubTask
            val viewStubTask = queue.removeAt(0)
            viewStubTask.inflate()
            iViewStubTask?.onTaskExecute(viewStubTask)
            //加载完成之后，再postOnAnimation 加载下一个
            ViewCompat.postOnAnimation(decorView,this)
        }else{
            iViewStubTask?.afterTaskExecute()
        }
    }

    fun notifyOnDetach(){
        list.forEach {
            it.onDetach()
        }
        list.clear()
    }

    fun notifyOnDataReady(){
        list.forEach {
            it.onDataReady()
        }
    }
}

interface IViewStubTask{

    fun beforeTaskExecute()

    fun onTaskExecute(viewStubTask: ViewStubTask)

    fun afterTaskExecute()
}