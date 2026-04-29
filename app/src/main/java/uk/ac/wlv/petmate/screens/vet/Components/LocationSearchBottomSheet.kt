package uk.ac.wlv.petmate.screens.vet.Components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.ac.wlv.petmate.data.model.NominatimResult
import uk.ac.wlv.petmate.viewmodel.VetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSearchBottomSheet(
    vetViewModel : VetViewModel,
    onLocationSelected: (lat: Double, lon: Double, name: String) -> Unit,
    onDismiss    : () -> Unit
) {
    val searchResults by vetViewModel.locationSearchResults.collectAsState()
    val isSearching   by vetViewModel.isSearching.collectAsState()
    var query         by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    // Auto focus search field when sheet opens
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor   = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(0.dp),
        sheetState       = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .padding(horizontal = 16.dp)
            ) {

                // ── Search Field ──────────────────────────────────────────────
                TextField(
                    value = query,
                    onValueChange = {
                        query = it
                        vetViewModel.searchLocation(it)
                    },
                    placeholder = {
                        Text(
                            text = "Search city, street, area...",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = {
                                query = ""
                                vetViewModel.clearLocationSearch()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear"
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ── Loading ───────────────────────────────────────────────────
                if (isSearching) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                // ── Results ───────────────────────────────────────────────────
                LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    items(searchResults, key = { it.place_id }) { result ->
                        LocationResultItem(
                            result = result,
                            onClick = {
                                onLocationSelected(
                                    result.lat.toDouble(),
                                    result.lon.toDouble(),
                                    result.display_name
                                )
                                onDismiss()
                            }
                        )
                    }
                }
            }
        }
    }
}

// ── Result Item ───────────────────────────────────────────────────────────────

@Composable
private fun LocationResultItem(
    result : NominatimResult,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier              = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 4.dp)
    ) {
        Icon(
            imageVector        = Icons.Default.LocationOn,
            contentDescription = null,
            tint               = MaterialTheme.colorScheme.primary,
            modifier           = Modifier.size(20.dp)
        )
        Column {
            // First part = place name
            Text(
                text       = result.display_name.split(",").first(),
                fontWeight = FontWeight.SemiBold,
                fontSize   = 14.sp,
                color      = MaterialTheme.colorScheme.onSurface
            )
            // Rest = full address
            Text(
                text     = result.display_name,
                fontSize = 12.sp,
                color    = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
}