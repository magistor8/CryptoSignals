package com.magistor8.cryptosignals.data.retrofit.entires

import com.google.gson.annotations.SerializedName

private const val NULL = ""

data class SignalsDataPOJO(
    @SerializedName("id"           ) var id           : String = NULL,
    @SerializedName("pair"         ) var pair         : String = NULL,
    @SerializedName("type"         ) var type         : String = NULL,
    @SerializedName("x"            ) var x            : String = NULL,
    @SerializedName("providerId"   ) var providerId   : String = NULL,
    @SerializedName("providerName" ) var providerName : String = NULL,
    @SerializedName("priceOpen"    ) var priceOpen    : String = NULL,
    @SerializedName("priceClose"   ) var priceClose   : String = NULL,
    @SerializedName("target1"      ) var target1      : String = NULL,
    @SerializedName("target2"      ) var target2      : String = NULL,
    @SerializedName("target3"      ) var target3      : String = NULL
)
