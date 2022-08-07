package com.magistor8.cryptosignals.data.retrofit

import com.magistor8.cryptosignals.data.retrofit.entires.ProviderDataPOJO
import com.magistor8.cryptosignals.data.retrofit.entires.SignalsDataPOJO
import kotlinx.coroutines.Deferred
import retrofit2.http.*


interface Api {
    @POST("getProviderDataFromIds")
    fun getProviderDataFromIds(
        @Body ids: List<String>
    ): Deferred<ProviderDataPOJO>

    @GET("getSignals")
    fun getSignals(): Deferred<List<SignalsDataPOJO>>
}
