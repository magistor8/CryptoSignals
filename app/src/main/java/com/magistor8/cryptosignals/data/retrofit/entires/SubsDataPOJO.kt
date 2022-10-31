package com.magistor8.cryptosignals.data.retrofit.entires

import com.google.gson.annotations.SerializedName

private const val NULL = ""

data class SubsDataPOJO(
    @SerializedName("id"       ) var id      : String = NULL,
    @SerializedName("user_id"  ) var userId  : String = NULL,
    @SerializedName("prov_id"  ) var provId  : String = NULL,
    @SerializedName("exp_time" ) var expTime : String = NULL
)
