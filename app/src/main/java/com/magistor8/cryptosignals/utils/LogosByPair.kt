package com.magistor8.cryptosignals.utils

import android.graphics.drawable.Drawable
import com.magistor8.cryptosignals.App
import com.magistor8.cryptosignals.R

object LogosByPair {
    private val logos: Map<String, Drawable> = mapOf(
        "BTC/USDT" to App.instance.applicationContext.resources.getDrawable(R.drawable.bitcoin,null)
    )

    fun getDrawable(pair: String): Drawable? {
        return if (logos.containsKey(pair)) {
            logos[pair]
        } else {
            null
        }
    }
}