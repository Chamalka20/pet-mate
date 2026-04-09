package uk.ac.wlv.petmate.data.datasources.remote

import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await
import uk.ac.wlv.petmate.data.model.ApiUser
import uk.ac.wlv.petmate.data.model.GoogleSignInRequest
import uk.ac.wlv.petmate.data.model.LoginRequest
import uk.ac.wlv.petmate.data.model.RegisterRequest
import uk.ac.wlv.petmate.data.network.ApiClient

class UserDataSource {

    private val auth = Firebase.auth

    // ── Google Sign-In ────────────────────────────────────
    suspend fun signInWithGoogle(account: GoogleSignInAccount): ApiUser {

        // Step 1 — Sign in to Firebase (same as before)
        val credential = GoogleAuthProvider.getCredential(
            account.idToken,
            null
        )

        val authResult = auth
            .signInWithCredential(credential)
            .await()

        val firebaseUser = authResult.user
            ?: throw Exception("Firebase user is null")

        // Step 2 — Get Firebase token
        val firebaseToken = firebaseUser
            .getIdToken(true)
            .await()
            .token
            ?: throw Exception("Failed to get Firebase token")

        // Step 3 — Send token to .NET API instead of Firestore
        val response = ApiClient.authApi.googleSignIn(
            GoogleSignInRequest(firebaseToken = firebaseToken)
        )

        if (response.isSuccessful) {
            val apiUser = response.body()?.user
                ?: throw Exception("Empty response from server")

            // Save JWT token locally
            saveToken(apiUser.token)

            Log.d("TAG", "User saved successfully via .NET API!")
            return apiUser

        } else {
            val errorBody = response.errorBody()?.string()
            Log.e("TAG", "API Error: $errorBody")
            throw Exception("Sign-in failed: $errorBody")
        }
    }

    // ── Normal Login ──────────────────────────────────────
    suspend fun login(email: String, password: String): ApiUser {

        val response = ApiClient.authApi.login(
            LoginRequest(email = email, password = password)
        )

        if (response.isSuccessful) {
            val apiUser = response.body()?.user
                ?: throw Exception("Empty response from server")

            saveToken(apiUser.token)
            return apiUser

        } else {
            throw Exception("Invalid email or password")
        }
    }

    // ── Register ──────────────────────────────────────────
    suspend fun register(
        fullName: String,
        email: String,
        password: String,
        phoneNumber: String? = null,
        profilePhotoUrl: String? = null
    ): ApiUser {

        val response =ApiClient.authApi.register(
            RegisterRequest(
                fullName = fullName,
                email = email,
                password = password,
                phoneNumber = phoneNumber,
                profilePhotoUrl = profilePhotoUrl
            )
        )

        if (response.isSuccessful) {
            val apiUser = response.body()?.user
                ?: throw Exception("Empty response from server")

            saveToken(apiUser.token)
            return apiUser

        } else {
            val errorBody = response.errorBody()?.string()
            throw Exception("Registration failed: $errorBody")
        }
    }

    // ── Get current user ──────────────────────────────────
    fun getCurrentUser(): ApiUser? {
        // Check if JWT token exists locally
        val token = getToken() ?: return null
        val firebaseUser = auth.currentUser ?: return null

        // Return basic user info from Firebase
        // For full info — call GET /api/profile (build this later)
        return ApiUser(
            id = firebaseUser.uid,
            fullName = firebaseUser.displayName ?: "",
            email = firebaseUser.email ?: "",
            phoneNumber = null,
            profilePhotoUrl = firebaseUser.photoUrl?.toString(),
            isGoogleUser = true,
            token = token,
            createdAt = ""
        )
    }

    // ── Sign out ──────────────────────────────────────────
    fun signOut() {
        auth.signOut()
        clearToken() // also clear saved JWT
    }

    // ── Token management (SharedPreferences) ──────────────
    private fun saveToken(token: String) {
        // Save to SharedPreferences
        // You need context for this — inject it or use a singleton
        Log.d("TAG", "JWT Token saved: $token")
    }

    private fun getToken(): String? {
        // Get from SharedPreferences
        return null
    }

    private fun clearToken() {
        // Clear from SharedPreferences
    }
}