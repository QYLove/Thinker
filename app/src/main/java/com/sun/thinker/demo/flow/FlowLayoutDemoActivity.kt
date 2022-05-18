package com.sun.thinker.demo.flow

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sun.thinker.R
import java.util.*

class FlowLayoutDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow_layout_demo)

        val tagGroup = findViewById<TagGroup>(R.id.activity_flow_layout_demo_tg)
        val texts: List<String> = Arrays.asList(
            "zhang",
            "phil",
            "csdn",
            "android",
            "zhang",
            "phil",
            "csdn",
            "android",
            "zhang",
            "phil",
            "csdn",
            "android"
        )
        val colors: List<Int> = Arrays.asList(
            Color.RED,
            Color.DKGRAY,
            Color.BLUE,
            Color.RED,
            Color.DKGRAY,
            Color.BLUE,
            Color.RED,
            Color.DKGRAY,
            Color.BLUE,
            Color.RED,
            Color.DKGRAY,
            Color.BLUE,
        )
        tagGroup.setTagView(texts, colors)


        val tagGroupKotlin = findViewById<TagGroupKotlin>(R.id.activity_flow_layout_demo_tgk)
        val textsK: MutableList<String> = Arrays.asList(
            "zhang",
            "phil",
            "csdn",
            "android",
            "zhang",
            "phil",
            "csdn",
            "android",
            "zhang",
            "phil",
            "csdn",
            "android"
        )
        val colorsK: MutableList<Int> = Arrays.asList(
            Color.RED,
            Color.DKGRAY,
            Color.BLUE,
            Color.RED,
            Color.DKGRAY,
            Color.BLUE,
            Color.RED,
            Color.DKGRAY,
            Color.BLUE,
            Color.RED,
            Color.DKGRAY,
            Color.BLUE,
        )
        tagGroupKotlin.setTagView(textsK,colorsK)
    }
}