package com.magistor8.cryptosignals.data

import com.magistor8.cryptosignals.data.retrofit.RemoteDataSource
import com.magistor8.cryptosignals.domain.entires.ProviderData
import com.magistor8.cryptosignals.domain.entires.SignalData
import com.magistor8.cryptosignals.domain.repo.SignalRepo
import java.util.concurrent.CopyOnWriteArrayList

class SignalRepoImpl(
    private val dataSource: RemoteDataSource
) : SignalRepo {

    override suspend fun getProviderDataFromIds(ids: List<String>): List<ProviderData> {
        return dataSource.getProviderDataFromIds(ids)
    }

    override suspend fun getSignals(): List<SignalData> {
        return dataSource.getSignals()
    }

}