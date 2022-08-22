package com.magistor8.cryptosignals.domain.repo

interface LoginRepo {
    suspend fun login(login: String, password: String): String
    fun loginCached(login: String)
}