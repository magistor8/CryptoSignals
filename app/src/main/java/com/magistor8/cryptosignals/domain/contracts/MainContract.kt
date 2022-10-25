package com.magistor8.cryptosignals.domain.contracts

import androidx.lifecycle.LiveData
import com.magistor8.cryptosignals.domain.entires.ProviderData
import com.magistor8.cryptosignals.domain.entires.SignalData
import com.magistor8.cryptosignals.domain.entires.SignalType
import java.util.concurrent.CopyOnWriteArrayList

interface MainContract {

    sealed interface ViewState {
        data class Error(val throwable: Throwable) : ViewState
        object Success : ViewState
    }

    sealed interface Events {
        data class CheckLogged(val login: String, val password: String) : Events
    }

    interface ViewModelInterface {
        val viewState: LiveData<ViewState>
        fun onEvent(event: Events)
    }
}