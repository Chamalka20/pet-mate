package uk.ac.wlv.petmate.data.repository
import uk.ac.wlv.petmate.data.model.ApiUser


interface AuthRepository {

    suspend fun GoogleSignInResult(
        idToken: String
    ): ApiUser

    suspend fun normalLogin(email: String, password: String): ApiUser

    suspend fun getCachedUser():ApiUser?
}