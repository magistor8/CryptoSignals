package com.magistor8.cryptosignals.data

import com.magistor8.cryptosignals.data.retrofit.RemoteDataSource
import com.magistor8.cryptosignals.domain.contracts.SignalsContract
import com.magistor8.cryptosignals.domain.entires.ProviderData
import com.magistor8.cryptosignals.domain.entires.SignalData
import com.magistor8.cryptosignals.domain.entires.SignalType
import com.magistor8.cryptosignals.domain.repo.SignalRepo
import kotlinx.coroutines.*

class SignalRepoImpl(
    private val dataSource: RemoteDataSource
) : SignalRepo {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    }

    private val coroutineContext = Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler
    private val coroutineScope = CoroutineScope(coroutineContext)

    override suspend fun getProviderDataFromIds(ids: List<String>): List<ProviderData> {
        return dataSource.getProviderDataFromIds(ids)
    }

    override suspend fun getSignals(setting: SignalsContract.FilterSettings): List<SignalData> {
        return filterSignalData(setting, dataSource.getSignals())
    }

    override suspend fun filterSignalData(setting: SignalsContract.FilterSettings, signals: List<SignalData>): List<SignalData> {

        val data = signals as MutableList<SignalData>
        var filteredData: MutableList<SignalData>

        filteredData = when (setting.type) {
            is SignalType.FutureLong -> data.filter { it.type is SignalType.FutureLong } as MutableList<SignalData>
            is SignalType.FutureShort -> data.filter { it.type is SignalType.FutureShort } as MutableList<SignalData>
            is SignalType.Spot -> data.filter { it.type is SignalType.Spot } as MutableList<SignalData>
            else -> data
        }
        //Если фильтр по провайдеру не пустой
        if (setting.earned != 0 ||
            setting.signals != 0 ||
            setting.registered != 0
        ) {
            filteredData = filteredData.filter { it.access } as MutableList<SignalData>
            //Получаем айдишники
            val ids = filteredData.map { it.providerId.toString() }

            //Получаем данные провайдеров по айдишникам
            val res = coroutineScope.async(coroutineExceptionHandler) {
                withContext(Dispatchers.IO) {
                    val providerData = getProviderDataFromIds(ids.distinct())
                    providerData
                }
            }

            return filterByProviderData(filteredData, res.await(), setting)

        } else {
            return filteredData
        }

    }

    override fun filterByProviderData(
        signalData: List<SignalData>,
        providerData: List<ProviderData>,
        setting: SignalsContract.FilterSettings
    ) :  List<SignalData> {
        val sd = signalData.toMutableList()
        var offset = 0
        signalData.forEachIndexed { index, elem ->
            val pd = providerData.find { it.id == elem.providerId }
            if (pd!!.earn30 < setting.earned ||
                pd.signals30 < setting.signals ||
                pd.registered < setting.registered
            ) {
                sd.remove(sd[index - offset])
                offset++
            }
        }
        return sd
    }

    override suspend fun getSubsId(login: String): List<Int> {
        return dataSource.getSubsId(login)
    }

    override fun setSignalAccess(data: List<SignalData>, subsId: List<Int>) {
        data.forEach {
            it.access = subsId.contains(it.providerId)
        }
    }
}

