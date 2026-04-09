package uk.ac.wlv.petmate.data.repository.impl

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import uk.ac.wlv.petmate.data.datasources.local.UserCache
import uk.ac.wlv.petmate.data.datasources.remote.UserDataSource
import uk.ac.wlv.petmate.data.model.ApiUser
import uk.ac.wlv.petmate.data.repository.AuthRepository

class AuthRepositoryImpl(
    private val userCache: UserCache,
    private val UserDataSource: UserDataSource
) : AuthRepository {

    override suspend fun GoogleSignInResult(
        task: Task<GoogleSignInAccount>
    ): ApiUser {

            val account = task.getResult(Exception::class.java)

            val user = UserDataSource.signInWithGoogle(account)
            // 💾 Cache locally
            userCache.saveUser(user)

            Result.success(user)

        return user
    }

    override suspend fun normalLogin(email: String, password: String): ApiUser{

        val user = UserDataSource.login(email,password)
        // 💾 Cache locally
        userCache.saveUser(user)
        Result.success(user)

        return user

    }

    override suspend fun getCachedUser(): ApiUser? {
        return userCache.getUser()
    }
}