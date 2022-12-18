package com.magistor8.cryptosignals.data.retrofit

import com.magistor8.cryptosignals.data.retrofit.entires.ProviderDataPOJO
import com.magistor8.cryptosignals.data.retrofit.entires.SignalsDataPOJO
import com.magistor8.cryptosignals.data.retrofit.entires.SubsDataPOJO
import kotlinx.coroutines.Deferred
import retrofit2.http.*


interface Api {
    @POST("getProviderDataFromIds")
    fun getProviderDataFromIds(
        @Body ids: List<String>
    ): Deferred<ProviderDataPOJO>

    @GET("getSignals")
    fun getSignals(): Deferred<List<SignalsDataPOJO>>

    @FormUrlEncoded
    @POST("getSubsById")
    fun getSubsById(
        @Field("login") login: String
    ): Deferred<Any>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("login") login: String,
        @Field("password") password: String,
    ): Deferred<String>

    @FormUrlEncoded
    @POST("checkLogged")
    fun checkLogged(
        @Field("login") login: String,
        @Field("password") password: String,
    ): Deferred<String>

    @GET("getProviderData")
    fun getProviderData(): Deferred<ProviderDataPOJO>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("login") login: String,
        @Field("password") password: String,
        @Field("email") email: String,
    ): Deferred<String>
}
