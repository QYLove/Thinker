package com.sun.thinker.demo.asyncInflate.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sun.thinker.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AsyncFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val startTime = System.currentTimeMillis()
        val homeFragmentOpen = AsyncUtils.isHomeFragmentOpen()
        val inflatedView: View

        val isOpen = AsyncUtils.isHomeFragmentOpen()
        if (isOpen) {
            AsyncUtils.asyncInflate(context)
        }

        return inflater.inflate(R.layout.fragment_asny, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AsyncFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}