package com.magistor8.cryptosignals.domain.repo

import com.magistor8.cryptosignals.domain.contracts.SignalsContract
import com.magistor8.cryptosignals.domain.entires.ProviderData
import com.magistor8.cryptosignals.domain.entires.SignalData
import java.util.concurrent.CopyOnWriteArrayList

interface SignalRepo {
    suspend fun getProviderDataFromIds(ids : List<String>): List<ProviderData>
    suspend fun getSignals(setting: SignalsContract.FilterSettings) : List<SignalData>
    suspend fun filterSignalData(setting: SignalsContract.FilterSettings, signals: List<SignalData>): List<SignalData>
    fun filterByProviderData(signalData: List<SignalData>, providerData: List<ProviderData>, setting: SignalsContract.FilterSettings): List<SignalData>
}