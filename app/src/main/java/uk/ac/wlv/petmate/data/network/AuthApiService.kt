package uk.ac.wlv.petmate.data.network

import retrofit2.http.Body
import retrofit2.Response
import retrofit2.http.POST
import uk.ac.wlv.petmate.data.model.AuthResponse
import uk.ac.wlv.petmate.data.model.GoogleSignInRequest
import uk.ac.wlv.petmate.data.model.LoginRequest
import uk.ac.wlv.petmate.data.model.RegisterRequest

interface AuthApiService {

    @POST("api/auth/google")
    suspend fun googleSignIn(
        @Body request: GoogleSignInRequest
    ): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>
}