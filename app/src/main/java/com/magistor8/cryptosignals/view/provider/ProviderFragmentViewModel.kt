package com.magistor8.cryptosignals.view.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.magistor8.cryptosignals.domain.BaseViewModel
import com.magistor8.cryptosignals.domain.contracts.ProviderContract
import com.magistor8.cryptosignals.domain.repo.ProviderRepo
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ProviderFragmentViewModel : ProviderContract.ViewModelInterface, BaseViewModel(), KoinComponent {

    private val repository : ProviderRepo by inject()
    override val viewState: LiveData<ProviderContract.ViewState> = MutableLiveData()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewState.mutable().postValue(ProviderContract.ViewState.Error(throwable))
    }

    override fun onEvent(event: ProviderContract.Events) {
        when(event) {
            is ProviderContract.Events.LoadData -> loadData(event.settings)
        }
    }

    private fun loadData(settings: ProviderContract.FilterSettings) {
        viewState.mutable().postValue(ProviderContract.ViewState.Loading)
        viewModelScope.launch(coroutineExceptionHandler) {
            withContext(Dispatchers.IO) {
                TODO("Остановился тут")
            }
        }
    }

}