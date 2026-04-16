package uk.ac.wlv.petmate.screens.vet.Components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.ac.wlv.petmate.core.utils.Constants.VET_SERVICES
import uk.ac.wlv.petmate.data.model.SortOption
import uk.ac.wlv.petmate.data.model.VetFilterState

// ─── Filter Bottom Sheet ──────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VetFilterBottomSheet(
    currentFilter: VetFilterState,
    onApply: (VetFilterState) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedServices  by remember { mutableStateOf(currentFilter.selectedServices) }
    var minRating         by remember { mutableStateOf(currentFilter.minRating) }
    var maxPrice          by remember { mutableStateOf(currentFilter.maxPrice) }
    var maxWaitingTime    by remember { mutableStateOf(currentFilter.maxWaitingTime) }
    var sortBy            by remember { mutableStateOf(currentFilter.sortBy) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // ── Header ────────────────────────────────────────────────────────
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Filter & Sort",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                TextButton(onClick = {
                    // Reset all filters
                    selectedServices = emptyList()
                    minRating        = 0f
                    maxPrice         = 5000
                    maxWaitingTime   = 60
                    sortBy           = SortOption.NONE
                }) {
                    Text(
                        text = "Reset",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
            Spacer(modifier = Modifier.height(16.dp))

            // ── Sort By ───────────────────────────────────────────────────────
            FilterSectionTitle(title = "Sort By")
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SortOption.entries.forEach { option ->
                    FilterChip(
                        selected  = sortBy == option,
                        onClick   = { sortBy = option },
                        label     = { Text(option.label, fontSize = 12.sp) },
                        colors    = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor     = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
            Spacer(modifier = Modifier.height(16.dp))

            // ── Services ──────────────────────────────────────────────────────
            FilterSectionTitle(title = "Services")
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                VET_SERVICES.forEach { service ->
                    FilterChip(
                        selected = selectedServices.contains(service),
                        onClick  = {
                            selectedServices = if (selectedServices.contains(service))
                                selectedServices - service
                            else
                                selectedServices + service
                        },
                        label  = { Text(service, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor     = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
            Spacer(modifier = Modifier.height(16.dp))

            // ── Min Rating ────────────────────────────────────────────────────
            FilterSectionTitle(title = "Minimum Rating: ${minRating.toBigDecimal().setScale(1, java.math.RoundingMode.HALF_UP)}⭐")
            Slider(
                value         = minRating,
                onValueChange = { minRating = it },
                valueRange    = 0f..5f,
                steps         = 9,
                colors        = SliderDefaults.colors(
                    thumbColor       = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary
                )
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("0", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                Text("5", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
            Spacer(modifier = Modifier.height(16.dp))

            // ── Max Price ─────────────────────────────────────────────────────
            FilterSectionTitle(title = "Max Price: LKR $maxPrice")
            Slider(
                value         = maxPrice.toFloat(),
                onValueChange = { maxPrice = it.toInt() },
                valueRange    = 0f..5000f,
                steps         = 49,
                colors        = SliderDefaults.colors(
                    thumbColor       = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary
                )
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("LKR 0",    fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                Text("LKR 5000", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
            Spacer(modifier = Modifier.height(16.dp))

            // ── Max Waiting Time ──────────────────────────────────────────────
            FilterSectionTitle(title = "Max Waiting Time: $maxWaitingTime mins")
            Slider(
                value         = maxWaitingTime.toFloat(),
                onValueChange = { maxWaitingTime = it.toInt() },
                valueRange    = 0f..60f,
                steps         = 11,
                colors        = SliderDefaults.colors(
                    thumbColor       = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary
                )
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("0 mins",  fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                Text("60 mins", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Apply Button ──────────────────────────────────────────────────
            Button(
                onClick = {
                    onApply(
                        VetFilterState(
                            selectedServices = selectedServices,
                            minRating        = minRating,
                            maxPrice         = maxPrice,
                            maxWaitingTime   = maxWaitingTime,
                            sortBy           = sortBy
                        )
                    )
                    onDismiss()
                },
                shape  = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor   = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text       = "Apply Filters",
                    fontWeight = FontWeight.Bold,
                    fontSize   = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ─── Section Title ────────────────────────────────────────────────────────────

@Composable
private fun FilterSectionTitle(title: String) {
    Text(
        text       = title,
        fontWeight = FontWeight.SemiBold,
        fontSize   = 14.sp,
        color      = MaterialTheme.colorScheme.onSurface
    )
}