package com.magistor8.cryptosignals.data

import com.magistor8.cryptosignals.data.retrofit.RemoteDataSource
import com.magistor8.cryptosignals.domain.entires.ProviderData
import com.magistor8.cryptosignals.domain.repo.ProviderRepo

class ProviderRepoImpl(private val dataSource: RemoteDataSource) : ProviderRepo {

    override suspend fun getData(): List<ProviderData> {
        return TODO()
    }

}
