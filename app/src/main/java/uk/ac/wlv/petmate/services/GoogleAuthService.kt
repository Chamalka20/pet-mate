package uk.ac.wlv.petmate.services

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.wlv.petmate.BuildConfig



// ── Service ───────────────────────────────────────────────────────────────────
class GoogleAuthService(private val context: Context) {

    private val credentialManager = CredentialManager.create(context)
    private val scope = CoroutineScope(Dispatchers.Main)

    // Returns idToken only — backend creates ApiUser
    suspend fun getGoogleIdToken(): String? {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.WEB_CLIENT_ID)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            val result = credentialManager.getCredential(
                request = request,
                context = context
            )
            val credential = result.credential
            if (credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                GoogleIdTokenCredential
                    .createFrom(credential.data)
                    .idToken
            } else null
        } catch (e: GetCredentialException) {
            null
        }
    }

    fun signOut() {
        scope.launch {
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        }
    }
}