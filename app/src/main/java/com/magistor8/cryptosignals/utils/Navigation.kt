package com.magistor8.cryptosignals.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.magistor8.cryptosignals.R

class Navigation(
    private val context: FragmentActivity,
) {

    enum class Action {
        ADD, REPLACE
    }

    fun <T : Fragment> check(fragmentClass: Class<T>): Fragment? {
        with(context.supportFragmentManager) {
            return findFragmentByTag(fragmentClass.name)
        }
    }

    fun <T : Fragment> navigate(
        fragmentClass: Class<T>,
        action: Action,
        bundle: Bundle? = null,
        addToBS: Boolean = false
    ) {

        val fragment = fragmentClass.newInstance()
        var forcedCreation = false
        bundle?.let {
            fragment.arguments = bundle
            forcedCreation = true
        }
        transaction(fragment, action, R.id.container, addToBS, fragmentClass.name, forcedCreation)
    }

    private fun transaction(
        fragment: Fragment,
        action: Action,
        container: Int,
        addToBS: Boolean,
        tag: String,
        forcedCreation: Boolean
    ) {
        with(context.supportFragmentManager) {
            val tr = beginTransaction()
            if (findFragmentByTag(tag) != null && !forcedCreation) {
                popBackStack(tag, 0)
            } else {
                when(action) {
                    Action.ADD -> {
                        tr.add(
                            container,
                            fragment,
                            tag
                        )
                    }
                    Action.REPLACE -> {
                        tr.replace(
                            container,
                            fragment,
                            tag
                        )
                    }
                }
            }
            if (addToBS) {
                tr.addToBackStack(tag)
            }
            tr.commit()
        }
    }

}
