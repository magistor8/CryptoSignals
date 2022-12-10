package com.magistor8.cryptosignals.domain.contracts

import android.os.Parcelable
import androidx.lifecycle.LiveData
import com.magistor8.cryptosignals.domain.entires.ProviderData
import com.magistor8.cryptosignals.domain.entires.SignalData
import com.magistor8.cryptosignals.domain.entires.SignalType
import kotlinx.android.parcel.Parcelize
import java.util.concurrent.CopyOnWriteArrayList

interface SignalsContract {


    sealed interface ViewState {
        object Loading : ViewState
        data class Error(val throwable: Throwable) : ViewState
        data class AllSignalsLoaded (val data: List<SignalData>) : ViewState
        data class ProviderDataLoaded(val data: List<ProviderData>) : ViewState
    }

    sealed interface Events {
        data class FilterType(val type: SignalType) : Events
        data class Filter(val earned: Int, val signals: Int, val registered: Int) : Events
    }

    interface ViewModelInterface {
        val viewState: LiveData<ViewState>
        val filterSettings : FilterSettings
        fun onEvent(event: Events)
    }

    data class FilterSettings (
        var type: SignalType,
        var earned : Int,
        var signals : Int,
        var registered: Int
    )
}