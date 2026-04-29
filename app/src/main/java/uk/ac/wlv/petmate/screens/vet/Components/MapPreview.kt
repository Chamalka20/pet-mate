package uk.ac.wlv.petmate.screens.vet.Components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import uk.ac.wlv.petmate.R
import uk.ac.wlv.petmate.core.utils.DrawableHelper


@Composable
fun MapPreview(
    latitude: Double?,
    longitude: Double?
) {

    if (latitude == null || longitude == null) return

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                MapView(ctx).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    zoomController.setVisibility(
                        org.osmdroid.views.CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT
                    )
                    setMultiTouchControls(true)

                    val startPoint = GeoPoint(latitude, longitude)
                    controller.setZoom(15.0)
                    controller.setCenter(startPoint)

                    val marker = Marker(this).apply {
                        position = startPoint
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        title = "Vet Location"
                        icon = DrawableHelper.resizeDrawable(context, R.drawable.ic_my_location, 110, 110)
                    }
                    overlays.add(marker)
                }
            }
        )
    }
}



