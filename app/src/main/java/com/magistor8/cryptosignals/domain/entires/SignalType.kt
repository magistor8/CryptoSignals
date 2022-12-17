package com.magistor8.cryptosignals.domain.entires

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class SignalType {
    object All : SignalType()
    object Spot : SignalType()
    data class FutureLong(
        val x : Int = 0
    ): SignalType()
    data class FutureShort(
        val x : Int = 0
    ): SignalType()
}