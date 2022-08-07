package com.magistor8.cryptosignals.domain.contracts

import androidx.lifecycle.LiveData
import com.magistor8.cryptosignals.domain.entires.ProviderData
import com.magistor8.cryptosignals.domain.entires.SignalData
import com.magistor8.cryptosignals.domain.entires.SignalType
import java.util.concurrent.CopyOnWriteArrayList

interface SignalsContract {

    sealed interface ViewState {
        object Loading : ViewState
        data class Error(val throwable: Throwable) : ViewState
        data class AllSignalsLoaded (val data: List<SignalData>) : ViewState
        data class ProviderDataLoaded(val data: List<ProviderData>) : ViewState
    }

    sealed interface Events {
        data class GetProviderDataFromIds(val ids : List<Int>): Events
        object GetSignals : Events
    }

    interface ViewModelInterface {
        val viewState: LiveData<ViewState>
        fun onEvent(event: Events)
    }

    interface FilterSettings {
        var type: SignalType
        var earned : Int
        var signals : Int
        var registered: Int
    }
}