package uk.ac.wlv.petmate.screens.vet.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import uk.ac.wlv.petmate.components.shimmers.shimmerBrush

@Composable
fun VetDetailsShimmer() {

    val brush = shimmerBrush()
    val widths = remember {
        List(5) { listOf(0.4f, 0.6f, 0.7f, 0.5f, 0.8f).random() }
    }
    Scaffold(){ padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // VetHeader shimmer
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Circle image
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(brush)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Name
                    Box(
                        modifier = Modifier
                            .width(140.dp)
                            .height(18.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(brush)
                    )
                    // Specialization
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(14.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(brush)
                    )
                    // Experience
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(14.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(brush)
                    )
                    // Rating
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(14.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(brush)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ServicesSection shimmer
            Box(
                modifier = Modifier
                    .width(70.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(brush)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(32.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(brush)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // VetInfoItem shimmers
            widths.forEach { width ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(brush)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(width)
                            .height(14.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(brush)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // "Clinic Details" title shimmer
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(18.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(brush)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // MapPreview shimmer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(brush)
            )
        }
    }
}