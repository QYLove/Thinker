package com.sun.thinker.demo.flow

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.JustifyContent

class TagGroupKotlin(context: Context, attrs: AttributeSet?) : FlexboxLayout(context, attrs) {

    private var mTexts: MutableList<String> = ArrayList()
    private var mColors: MutableList<Int> = ArrayList()
    private var TAG_VIEW_COUNT: Int = 0


    init {
        this.flexDirection = FlexDirection.ROW
        this.justifyContent = JustifyContent.FLEX_START
        this.flexWrap = FlexWrap.WRAP
    }

    fun setTagView(@NonNull texts: MutableList<String>, @Nullable colors: MutableList<Int>) {
        if (texts == null || texts.isEmpty()) {
            throw RuntimeException("tag view文本字段不能为空")
        }

        this.mTexts = texts
        TAG_VIEW_COUNT = texts.size

        if (colors == null || colors.isEmpty()) {
            for (i in 0 until TAG_VIEW_COUNT step 1) {
                mColors.clear();
                mColors.add(Color.WHITE)
            }
        } else {
            this.mColors = colors
        }

        this.removeAllViews()

        for (i in 0 until TAG_VIEW_COUNT step 1) {
            var textView = makeTextView(mTexts[i], mColors[i])
            var params =
                LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            //设置每一个子View在整体布局中与其他子View的上下左右的margin
            params.setMargins(0, 1, 5, 1)

            this.addView(textView, params)
        }

        this.invalidate()
    }

    fun makeTextView(s: String, c: Int): TextView {
        var textView = TextView(context)
        textView.text = s
        textView.setPadding(10, 5, 10, 5)

        val strokeWidth = 5
        val roundRadius = 15f
        val strokeColor = Color.GRAY
        var fillColor = c

        var gd = GradientDrawable()
        gd.setColor(fillColor)
        gd.setCornerRadius(roundRadius)
        gd.setStroke(strokeWidth, strokeColor)

        textView.background = gd

        return textView
    }
}