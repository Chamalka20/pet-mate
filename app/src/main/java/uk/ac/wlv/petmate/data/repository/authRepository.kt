package uk.ac.wlv.petmate.data.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import uk.ac.wlv.petmate.data.model.ApiUser


interface AuthRepository {

    suspend fun GoogleSignInResult(
        task: Task<GoogleSignInAccount>
    ): ApiUser

    suspend fun normalLogin(email: String, password: String): ApiUser

    suspend fun getCachedUser():ApiUser?
}