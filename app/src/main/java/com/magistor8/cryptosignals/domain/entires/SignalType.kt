package com.magistor8.cryptosignals.domain.entires

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class SignalType: Parcelable {
    @Parcelize
    object All : SignalType(), Parcelable
    @Parcelize
    object Spot : SignalType(), Parcelable
    @Parcelize
    data class FutureLong(
        val x : Int = 0
    ): SignalType(), Parcelable
    @Parcelize
    data class FutureShort(
        val x : Int = 0
    ): SignalType(), Parcelable
}