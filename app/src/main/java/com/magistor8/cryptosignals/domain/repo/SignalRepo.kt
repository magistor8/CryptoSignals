package com.magistor8.cryptosignals.domain.repo

import com.magistor8.cryptosignals.domain.entires.ProviderData
import com.magistor8.cryptosignals.domain.entires.SignalData
import java.util.concurrent.CopyOnWriteArrayList

interface SignalRepo {
    suspend fun getProviderDataFromIds(ids : List<String>): List<ProviderData>
    suspend fun getSignals() : List<SignalData>
}