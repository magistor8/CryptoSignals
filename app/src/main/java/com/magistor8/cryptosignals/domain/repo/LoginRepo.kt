package com.magistor8.cryptosignals.domain.repo

interface LoginRepo {
    suspend fun login(login: String, password: String): String
    suspend fun loginCached(login: String, password: String)
    suspend fun checkLogged(login: String, password: String): String
    suspend fun register(login: String, password: String, email: String): String
}