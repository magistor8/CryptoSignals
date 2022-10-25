package com.magistor8.cryptosignals.view


import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.magistor8.cryptosignals.R


abstract class BaseFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            it.findViewById<FrameLayout>(R.id.loading_layout).visibility = GONE
        }
    }

    fun loadingScreen(v: Int) {
        activity?.let {
            val loadingLayout = it.findViewById<FrameLayout>(R.id.loading_layout)
            if (v == VISIBLE) {
                loadingLayout.visibility = VISIBLE
            } else {
                loadingLayout.visibility = GONE
            }
        }
    }
}