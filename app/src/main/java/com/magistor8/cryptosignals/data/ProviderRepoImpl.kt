package com.magistor8.cryptosignals.data

import com.magistor8.cryptosignals.data.retrofit.RemoteDataSource
import com.magistor8.cryptosignals.domain.contracts.ProviderContract
import com.magistor8.cryptosignals.domain.entires.ProviderData
import com.magistor8.cryptosignals.domain.entires.SignalData
import com.magistor8.cryptosignals.domain.repo.ProviderRepo

class ProviderRepoImpl(private val dataSource: RemoteDataSource) : ProviderRepo {

    override suspend fun getData(settings: ProviderContract.FilterSettings): List<ProviderData> {
        return filterProviderData(settings, dataSource.getProviderData())
    }

    private fun filterProviderData(settings: ProviderContract.FilterSettings, providerData: List<ProviderData>): List<ProviderData> {

        var data = providerData as MutableList<ProviderData>

        if (settings.name != "") {
            data = data.filter { it.name.lowercase().contains(settings.name.trim().lowercase()) || it.name == settings.name } as MutableList<ProviderData>
        }

        if (settings.registered > 0) {
            data = data.filter { it.registered >= settings.registered } as MutableList<ProviderData>
        }

        if (settings.earned > 0) {
            data = data.filter {
                when(settings.earnedPeriod) {
                    "7 days" -> it.earn7 >= settings.earned
                    "30 days" -> it.earn30 >= settings.earned
                    "All" -> it.earnAll >= settings.earned
                    else -> false
                }
            } as MutableList<ProviderData>
        }

        if (settings.signals > 0) {
            data = data.filter {
                when(settings.signalsPeriod) {
                    "30 days" -> it.signals30 >= settings.signals
                    "All" -> it.signalsAll >= settings.signals
                    else -> false
                }
            } as MutableList<ProviderData>
        }

        return data
    }

}
