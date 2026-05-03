package uk.ac.wlv.petmate.data.datasources.local

import uk.ac.wlv.petmate.data.model.ApiUser


interface UserCache {
    suspend fun saveUser(user: ApiUser)
    suspend fun getUser(): ApiUser?
    suspend fun getToken(): String?
    suspend fun saveToken(token: String)
    suspend fun clearUser()
    suspend fun clearToken()
    suspend fun clear()
}
