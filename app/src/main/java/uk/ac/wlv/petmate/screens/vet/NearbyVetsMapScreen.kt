package uk.ac.wlv.petmate.screens.vet

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.permissions.rememberPermissionState
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import uk.ac.wlv.petmate.data.model.Vet
import uk.ac.wlv.petmate.ui.theme.StarYellow
import uk.ac.wlv.petmate.viewmodel.VetViewModel
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import kotlinx.coroutines.launch
import org.osmdroid.views.overlay.Marker
import uk.ac.wlv.petmate.components.NetworkCircleImage
import org.osmdroid.util.GeoPoint
import uk.ac.wlv.petmate.R
import uk.ac.wlv.petmate.core.utils.DrawableHelper
import uk.ac.wlv.petmate.screens.vet.Components.LocationSearchBottomSheet


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun NearbyVetsMapScreen(
    vetViewModel : VetViewModel,
    onVetClick   : (Vet) -> Unit = {},
    navController: NavHostController,
    onBack       : () -> Unit    = { navController.popBackStack()}
) {

    val locationPerm = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    var showSearchSheet by remember { mutableStateOf(false) }
    var searchBarText by remember { mutableStateOf("Search this area...") }
    // Default location — Cairo
    val nearbyVets   by vetViewModel.nearbyVets.collectAsState()
    val userLocation by vetViewModel.userLocation.collectAsState()
    var selectedVet  by remember { mutableStateOf<Vet?>(null) }
    var osmMap       by remember { mutableStateOf<MapView?>(null) }
    val scope = rememberCoroutineScope()
    var osmMapState by remember { mutableStateOf<MapView?>(null) }
    val context = LocalContext.current



    fun fetchLocation() {
        searchBarText = "Search this area..."
        vetViewModel.clearLocationSearch()
        scope.launch {
            vetViewModel.fetchUserLocation()

        }
    }

    // ── GPS dialog launcher ───────────────────────────────────────────────
    val gpsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            fetchLocation()
        }
    }

    // ── Check permission + GPS on launch ──────────────────────────────────
    LaunchedEffect(locationPerm.status.isGranted) {
        if (locationPerm.status.isGranted) {
            vetViewModel.checkGpsSettings(
                onResolvable = { exception ->
                    gpsLauncher.launch(
                        IntentSenderRequest.Builder(exception.resolution).build()
                    )
                },
                onAlreadyOn = { fetchLocation() }
            )
        } else {
            locationPerm.launchPermissionRequest()
        }
    }
    // Add vet markers whenever map or vets change
    LaunchedEffect(Unit) {
        snapshotFlow { osmMapState to userLocation }
            .collect { (map, loc) ->
                map ?: return@collect
                loc  ?: return@collect

                val (lat, lon) = loc

                // ── Move camera ───────────────────────────────────────────
                map.controller.animateTo(GeoPoint(lat, lon))
                map.controller.setZoom(14.0)

                // ── Update markers ────────────────────────────────────────
                map.overlays.removeAll { it is Marker }

                map.overlays.add(
                    Marker(map).apply {
                        position = GeoPoint(lat, lon)
                        title    = "You are here"
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        icon = DrawableHelper.resizeDrawable(context, R.drawable.ic_my_location, 110, 110)
                    }
                )

                nearbyVets.forEach { (vet, dist) ->
                    val distLabel = if (dist < 1.0) "${(dist * 1000).toInt()} m"
                    else            "${"%.1f".format(dist)} km"
                    map.overlays.add(
                        Marker(map).apply {
                            position = GeoPoint(vet.latitude!!, vet.longitude!!)
                            title    = vet.name
                            snippet  = distLabel
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            icon = DrawableHelper.resizeDrawable(context, R.drawable.ic_vet_marker, 110, 110)
                            setOnMarkerClickListener { _, _ ->
                                selectedVet = vet
                                true
                            }
                        }
                    )
                }
                map.invalidate()
            }
    }

    if (showSearchSheet) {
        LocationSearchBottomSheet(
            vetViewModel      = vetViewModel,
            onLocationSelected = { lat, lon, name ->
                vetViewModel.updateUserLocation(lat, lon)
                searchBarText = name
            },
            onDismiss = { showSearchSheet = false }
        )
    }


    Box(modifier = Modifier.fillMaxSize()) {

        // ── osmdroid MapView ──────────────────────────────────────────────
        AndroidView(
            factory = { ctx ->
                Configuration.getInstance().load(
                    ctx,
                    ctx.getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
                )
                Configuration.getInstance().userAgentValue = ctx.packageName

                MapView(ctx).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)
                    controller.setZoom(14.0)
                    osmMapState = this
                }
            },
            update   = { map -> map.onResume() },
            modifier = Modifier.fillMaxSize()
        )

        // ── Back Button ───────────────────────────────────────────────────
        IconButton(
            onClick  = onBack,
            modifier = Modifier
                .padding(16.dp)
                .statusBarsPadding()
                .align(Alignment.TopStart)
                .background(Color.White, CircleShape)
                .size(40.dp)
        ) {
            Icon(
                imageVector        = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint               = Color.Black
            )
        }

        // ── Search This Area Button ───────────────────────────────────────

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 12.dp, start = 64.dp, end = 16.dp)
                .fillMaxWidth()
                .height(46.dp)
                .background(Color.White, RoundedCornerShape(24.dp))
                .clickable { showSearchSheet = true }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector        = Icons.Default.Search,
                    contentDescription = null,
                    tint               = if (searchBarText == "Search this area...")
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    else
                        MaterialTheme.colorScheme.primary,
                    modifier           = Modifier.size(18.dp)
                )
                Text(
                    text     = searchBarText,
                    fontSize = 13.sp,
                    color    = if (searchBarText == "Search this area...")
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    else
                        MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                if (searchBarText != "Search this area...") {
                    IconButton(
                        onClick = {
                            searchBarText = "Search this area..."
                            vetViewModel.clearLocationSearch()
                        },
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
// ── My Location FAB ───────────────────────────────────────────────────
        FloatingActionButton(
            onClick = { fetchLocation() },
            shape   = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor   = Color.White,
            modifier       = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 220.dp)
        ) {
            Icon(
                imageVector        = Icons.Default.MyLocation,
                contentDescription = "My Location",
                modifier           = Modifier.size(24.dp)
            )
        }

        // ── Bottom Vet Cards Carousel ─────────────────────────────────────
        if (nearbyVets.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding        = PaddingValues(horizontal = 16.dp),
                modifier              = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
            ) {
                items(nearbyVets, key = { it.first.id }) { (vet, distKm) ->
                    NearbyVetCard(
                        vet        = vet,
                        distanceKm = distKm,
                        isSelected = selectedVet?.id == vet.id,
                        onClick    = {
                            selectedVet = vet
                            osmMapState?.controller?.animateTo(
                                GeoPoint(vet.latitude!!, vet.longitude!!)
                            )
                            osmMapState?.controller?.setZoom(14.0)
                        }
                    )
                }
            }
        }
    }

    // Lifecycle cleanup
    DisposableEffect(Unit) {
        onDispose { osmMap?.onDetach() }
    }
}

// ── Nearby Vet Card ───────────────────────────────────────────────────────────

@Composable
private fun NearbyVetCard(
    vet        : Vet,
    distanceKm : Double,
    isSelected : Boolean    = false,
    onClick    : () -> Unit = {}
) {
    val distLabel = if (distanceKm < 1.0)
        "${(distanceKm * 1000).toInt()} m"
    else
        "${"%.1f".format(distanceKm)} km"

    Card(
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        ),
        modifier  = Modifier
            .width(200.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                NetworkCircleImage(
                    imageUrl           = vet.imageUrl,
                    contentDescription = vet.name ?: "",

                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text       = vet.name ?: "Unknown",
                        fontWeight = FontWeight.Bold,
                        fontSize   = 13.sp,
                        maxLines   = 1,
                        overflow   = TextOverflow.Ellipsis
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector        = Icons.Default.Star,
                            contentDescription = null,
                            tint               = StarYellow,
                            modifier           = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text       = vet.rating.toString(),
                            fontSize   = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector        = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint               = MaterialTheme.colorScheme.primary,
                    modifier           = Modifier.size(12.dp)
                )
                Text(
                    text     = vet.location ?: "Unknown",
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color    = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier              = Modifier.fillMaxWidth()
            ) {
                Text(
                    text  = "Price: ${vet.price ?: 0} EGP",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text       = distLabel,
                    fontSize   = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

// ── Haversine Distance ────────────────────────────────────────────────────────

fun haversineDistance(
    lat1: Double, lon1: Double,
    lat2: Double, lon2: Double
): Double {
    val R    = 6371.0
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a    = sin(dLat / 2).pow(2) +
            cos(Math.toRadians(lat1)) *
            cos(Math.toRadians(lat2)) *
            sin(dLon / 2).pow(2)
    return R * 2 * asin(sqrt(a))
}