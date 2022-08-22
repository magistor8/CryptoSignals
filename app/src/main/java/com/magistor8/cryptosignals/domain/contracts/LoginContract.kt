package com.magistor8.cryptosignals.domain.contracts

import androidx.lifecycle.LiveData
import com.magistor8.cryptosignals.domain.entires.ProviderData
import com.magistor8.cryptosignals.domain.entires.SignalData
import com.magistor8.cryptosignals.domain.entires.SignalType
import java.util.concurrent.CopyOnWriteArrayList

interface LoginContract {

    sealed interface ViewState {
        object Loading : ViewState
        data class Error(val throwable: Throwable) : ViewState
        data class Logged(val message: String) : ViewState
    }

    sealed interface Events {
        data class Login(val login: String, val password: String) : Events
        data class Register(val login: String, val email: String, val password: String) : Events
    }

    interface ViewModelInterface {
        val viewState: LiveData<ViewState>
        fun onEvent(event: Events)
    }
}