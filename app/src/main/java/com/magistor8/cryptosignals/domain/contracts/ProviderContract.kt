package com.magistor8.cryptosignals.domain.contracts

import androidx.lifecycle.LiveData

interface ProviderContract {

    sealed interface ViewState {
        object Loading : ViewState
        data class Error(val throwable: Throwable) : ViewState
    }

    sealed interface Events {
        data class LoadData(val settings: FilterSettings): Events
    }

    interface ViewModelInterface {
        val viewState: LiveData<ViewState>
        fun onEvent(event: Events)
    }

    data class FilterSettings (
        var name: String,
        var earnedPeriod : Int,
        var earned : Int,
        var signalsPeriod : Int,
        var signals : Int,
        var registered: Int
    )
}