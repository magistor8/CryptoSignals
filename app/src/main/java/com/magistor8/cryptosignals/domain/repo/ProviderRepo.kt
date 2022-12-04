package com.magistor8.cryptosignals.domain.repo

import com.magistor8.cryptosignals.domain.contracts.ProviderContract
import com.magistor8.cryptosignals.domain.entires.ProviderData

interface ProviderRepo {
    suspend fun getData(settings: ProviderContract.FilterSettings): List<ProviderData>
}