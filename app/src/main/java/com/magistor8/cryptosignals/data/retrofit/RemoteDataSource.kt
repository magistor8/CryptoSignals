package com.magistor8.cryptosignals.data.retrofit

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.magistor8.cryptosignals.App
import com.magistor8.cryptosignals.data.retrofit.entires.ProviderDataPOJO
import com.magistor8.cryptosignals.domain.contracts.ProviderContract
import com.magistor8.cryptosignals.domain.entires.ProviderData
import com.magistor8.cryptosignals.domain.entires.SignalData
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.CopyOnWriteArrayList


class RemoteDataSource {

    private val api = Retrofit.Builder()
        .baseUrl(App.instance.baseUri)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(Api::class.java)

    suspend fun getProviderDataFromIds(ids: List<String>) : List<ProviderData> = RetrofitConverter.pojoToProviderData(api.getProviderDataFromIds(ids).await())
    suspend fun getProviderData() : List<ProviderData> = RetrofitConverter.pojoToProviderData(api.getProviderData().await())

    suspend fun getSignals(): List<SignalData> = RetrofitConverter.pojoToSignalsData(api.getSignals().await())

    suspend fun login(login: String, password: String): String = api.login(login, password).await()
    suspend fun checkLogged(login: String, password: String): String = api.checkLogged(login, password).await()
    suspend fun getSubsId(login: String): List<Int> = RetrofitConverter.pojoToSubsId(api.getSubsById(login).await())

}