package com.magistor8.cryptosignals.data.retrofit

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.magistor8.cryptosignals.data.retrofit.entires.ProviderDataPOJO
import com.magistor8.cryptosignals.domain.entires.ProviderData
import com.magistor8.cryptosignals.domain.entires.SignalData
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.CopyOnWriteArrayList


class RemoteDataSource {

    companion object {
        private const val BASE_URL = "https://magistor8.tk/"
    }

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(Api::class.java)

    suspend fun getProviderDataFromIds(ids: List<String>) : List<ProviderData> = RetrofitConverter.pojoToProviderData(api.getProviderDataFromIds(ids).await())
    suspend fun getSignals(): List<SignalData> = RetrofitConverter.pojoToSignalsData(api.getSignals().await())
    suspend fun login(login: String, password: String): String = api.login(login, password).await()
    suspend fun checkLogged(login: String, password: String): String = api.checkLogged(login, password).await()

}