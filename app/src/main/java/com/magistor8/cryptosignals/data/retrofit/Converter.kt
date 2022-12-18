package com.magistor8.cryptosignals.data.retrofit

import android.telephony.SubscriptionPlan
import com.google.gson.internal.LinkedTreeMap
import com.magistor8.cryptosignals.App
import com.magistor8.cryptosignals.R
import com.magistor8.cryptosignals.data.retrofit.entires.ProviderDataPOJO
import com.magistor8.cryptosignals.data.retrofit.entires.SignalsDataPOJO
import com.magistor8.cryptosignals.data.retrofit.entires.SubsDataPOJO
import com.magistor8.cryptosignals.domain.entires.ProviderData
import com.magistor8.cryptosignals.domain.entires.SignalData
import com.magistor8.cryptosignals.domain.entires.SignalType
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.time.measureTime

object RetrofitConverter {

    fun pojoToSignalsData(signals: List<SignalsDataPOJO>): List<SignalData> {
        val newList: MutableList<SignalData> = mutableListOf()
        signals.onEach {
            newList.add(SignalData(
                    it.id.toInt(),
                App.instance.photoUri + it.pair,
                    it.pair,
                    when(it.type) {
                        "Spot" -> SignalType.Spot
                        "FutureLong" -> SignalType.FutureLong(it.x.toInt())
                        "FutureShort" -> SignalType.FutureShort(it.x.toInt())
                        else -> SignalType.Spot
                    },
                    it.providerId.toInt(),
                    it.providerName,
                    it.priceOpen.toDouble(),
                    it.priceClose.toDouble(),
                    it.target1.toDouble(),
                    it.target2.toDouble(),
                    it.target3.toDouble(),
                    false
                )
            )
        }
        return newList
    }

    fun pojoToProviderData(providers: ProviderDataPOJO): List<ProviderData> {
        val newList: MutableList<ProviderData> = mutableListOf()
        providers.result.onEach {
            newList.add(ProviderData(
                it.id.toInt(),
                it.name,
                App.instance.photoUri + it.logo,
                it.registered.toInt(),
                it.earn7.toInt(),
                it.earn30.toInt(),
                it.earnAll.toInt(),
                it.signals30.toInt(),
                it.signalsAll.toInt(),
                true,
                0
            ))
        }
        return newList
    }

    fun pojoToSubsId(subs: Any): List<Int> {
        if (subs is List<*>) {
            val ids : MutableList<Int> = mutableListOf()
            subs.onEach {
                ids.add((it as LinkedTreeMap<String, String>)["prov_id"]?.toInt() ?: 0)
            }
            return ids
        }
        return emptyList()
    }

}