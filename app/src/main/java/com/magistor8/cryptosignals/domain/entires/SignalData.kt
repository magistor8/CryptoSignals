package com.magistor8.cryptosignals.domain.entires

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class SignalData(
    val id: Int,
    val logo: String,
    val pair: String,
    val type: SignalType,
    val providerId: Int,
    val providerName: String,
    val priceOpen: Double,
    val priceClose: Double,
    val target1: Double,
    val target2: Double,
    val target3: Double,
    var access: Boolean
)