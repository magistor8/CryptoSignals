package com.magistor8.cryptosignals.domain.entires

data class ProviderData(
    val id: Int,
    val name: String,
    val logo: String,
    val registered: Int,
    val earn7: Int,
    val earn30: Int,
    val earnAll: Int,
    val signals30: Int,
    val signalsAll: Int,
    val access: Boolean
)
