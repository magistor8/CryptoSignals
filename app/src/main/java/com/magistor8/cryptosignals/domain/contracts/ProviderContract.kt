package com.magistor8.cryptosignals.domain.contracts

import androidx.lifecycle.LiveData
import com.magistor8.cryptosignals.domain.entires.ProviderData
import com.magistor8.cryptosignals.domain.entires.Sort

interface ProviderContract {

    sealed interface ViewState {
        object Loading : ViewState
        data class Error(val throwable: Throwable) : ViewState
        data class ProviderDataSuccess(val data : List<ProviderData>) : ViewState
    }

    sealed interface Events {
        data class LoadData(val earnPeriod: String,val earn: Int,val signalPeriod: String,val signal: Int,val name: String,val register: Int): Events
    }

    interface ViewModelInterface {
        val filterSetting: FilterSettings
        val viewState: LiveData<ViewState>
        fun onEvent(event: Events)
    }

    data class FilterSettings (
        var name: String,
        var earnedPeriod : String,
        var earned : Int,
        var signalsPeriod : String,
        var signals : Int,
        var registered: Int
    )
}