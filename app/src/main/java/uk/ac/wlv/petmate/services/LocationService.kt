package uk.ac.wlv.petmate.services

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import kotlinx.coroutines.suspendCancellableCoroutine

data class UserLocation(
    val latitude  : Double,
    val longitude : Double
)

class LocationService(private val context: Context) {

    private val fusedClient   = LocationServices.getFusedLocationProviderClient(context)
    private val settingsClient = LocationServices.getSettingsClient(context)

    // ── Check if GPS is enabled ───────────────────────────────────────────
    fun isGpsEnabled(): Boolean {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    // ── Build GPS enable request ──────────────────────────────────────────
    // Returns ResolvableApiException if GPS is off so screen can launch dialog
    fun checkGpsSettings(
        onResolvable : (ResolvableApiException) -> Unit,
        onAlreadyOn  : () -> Unit
    ) {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L)
            .build()

        val settingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()

        settingsClient.checkLocationSettings(settingsRequest)
            .addOnSuccessListener {
                onAlreadyOn()             // GPS already on
            }
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    onResolvable(exception) // GPS off → screen launches default dialog
                }
            }
    }

    // ── Get last known location ───────────────────────────────────────────
    @SuppressLint("MissingPermission")
    suspend fun getLastLocation(): UserLocation? =
        suspendCancellableCoroutine { cont ->
            fusedClient.lastLocation
                .addOnSuccessListener { loc ->
                    cont.resumeWith(
                        Result.success(
                            loc?.let { UserLocation(it.latitude, it.longitude) }
                        )
                    )
                }
                .addOnFailureListener {
                    cont.resumeWith(Result.success(null))
                }

            cont.invokeOnCancellation {
                // optional cleanup
            }
        }

    // ── Get current location ──────────────────────────────────────────────
    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): UserLocation? =
        suspendCancellableCoroutine { cont ->
            fusedClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null
            )
                .addOnSuccessListener { loc ->
                    cont.resumeWith(
                        Result.success(
                            loc?.let { UserLocation(it.latitude, it.longitude) }
                        )
                    )
                }
                .addOnFailureListener {
                    cont.resumeWith(Result.success(null))
                }

            cont.invokeOnCancellation {
                // optional cleanup
            }
        }

    // ── Get best available location ───────────────────────────────────────
    suspend fun getBestLocation(): UserLocation? =
        getCurrentLocation() ?: getLastLocation()
}