package uk.ac.wlv.petmate.screens.mainScreens.home.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import uk.ac.wlv.petmate.components.shimmers.shimmerBrush

@Composable
fun VetCardShimmer() {


    val brush = shimmerBrush()

    Card(
        modifier = Modifier
            .width(180.dp)
            .padding(vertical = 12.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Circle image
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(brush)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Rating row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(14.dp).clip(RoundedCornerShape(7.dp)).background(brush))
                Spacer(modifier = Modifier.width(4.dp))
                Box(modifier = Modifier.width(30.dp).height(12.dp).clip(RoundedCornerShape(7.dp)).background(brush))
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Location row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(14.dp).clip(RoundedCornerShape(5.dp)).background(brush))
                Spacer(modifier = Modifier.width(4.dp))
                Box(modifier = Modifier.width(70.dp).height(12.dp).clip(RoundedCornerShape(7.dp)).background(brush))
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Price row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(14.dp).clip(RoundedCornerShape(5.dp)).background(brush))
                Spacer(modifier = Modifier.width(4.dp))
                Box(modifier = Modifier.width(60.dp).height(12.dp).clip(RoundedCornerShape(7.dp)).background(brush))
            }
        }
    }
}


@Composable
fun VetCardShimmerRow() {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(4) {
            VetCardShimmer()
        }
    }
}