package com.magistor8.cryptosignals.data.retrofit.entires

import com.google.gson.annotations.SerializedName

data class ProviderDataPOJO (
    @SerializedName("result" ) var result : List<Result>
)

data class Result (
    @SerializedName("id"         ) var id         : String,
    @SerializedName("name"       ) var name       : String,
    @SerializedName("logo"       ) var logo       : String,
    @SerializedName("registered" ) var registered : Long,
    @SerializedName("earn7"      ) var earn7      : String,
    @SerializedName("earn30"     ) var earn30     : String,
    @SerializedName("earnAll"    ) var earnAll    : String,
    @SerializedName("signals30"  ) var signals30  : String,
    @SerializedName("signalsAll" ) var signalsAll : String
)