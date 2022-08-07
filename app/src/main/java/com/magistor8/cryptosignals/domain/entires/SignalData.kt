package com.magistor8.cryptosignals.domain.entires

import android.graphics.drawable.Drawable

data class SignalData(
    val id: Int,
    val logo: Drawable,
    val pair: String,
    val type: SignalType,
    val providerId: Int,
    val providerName: String,
    val priceOpen: Double,
    val priceClose: Double,
    val target1: Double,
    val target2: Double,
    val target3: Double,
    val access: Boolean
)