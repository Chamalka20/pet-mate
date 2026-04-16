package uk.ac.wlv.petmate.screens.vet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import uk.ac.wlv.petmate.components.NetworkCircleImage
import uk.ac.wlv.petmate.core.UiState
import uk.ac.wlv.petmate.data.model.Vet
import uk.ac.wlv.petmate.ui.theme.AvailableGreen
import uk.ac.wlv.petmate.ui.theme.StarYellow
import uk.ac.wlv.petmate.viewmodel.VetViewModel
import uk.ac.wlv.petmate.core.utils.Constants.VET_SERVICES
import uk.ac.wlv.petmate.data.model.SortOption
import uk.ac.wlv.petmate.screens.vet.Components.VetFilterBottomSheet
import java.util.UUID


// ─── Main Screen ──────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VetsListScreen(
    onVetClick: (Vet) -> Unit = {},
    onBookAppointment: (Vet) -> Unit = {},
    navController: NavHostController,
    vetViewModel: VetViewModel,

) {
    val vetListState by vetViewModel.vetListState.collectAsState()
    val isLoadingMore by vetViewModel.isLoadingMore.collectAsState()
    val searchQuery by vetViewModel.searchQuery.collectAsState()

    var showFilterSheet by remember { mutableStateOf(false) }
    val filterState    by vetViewModel.filterState.collectAsState()

// Show active filter count badge on Filter chip
    val activeFilterCount = remember(filterState) {
        var count = 0
        if (filterState.selectedServices.isNotEmpty()) count++
        if (filterState.minRating > 0f) count++
        if (filterState.maxPrice < 5000) count++
        if (filterState.maxWaitingTime < 60) count++
        if (filterState.sortBy != SortOption.NONE) count++
        count
    }


    if (showFilterSheet) {
        VetFilterBottomSheet(
            currentFilter = filterState,
            onApply       = { vetViewModel.applyFilter(it) },
            onDismiss     = { showFilterSheet = false }
        )
    }
    Scaffold(
        topBar = { VetsTopBar(onBackClick = { navController.popBackStack()}) },
        // background → PetBackground (light) / PetBackgroundDark (dark)
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            VetSearchBar(
                query = searchQuery,
                onQueryChange = { vetViewModel.onSearchQueryChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            FilterSortRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                activeFilterCount = activeFilterCount,
                onFilterClick     = { showFilterSheet = true }
            )

            when (val state = vetListState) {

                // Loading first page
                is UiState.Loading -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Success — show list
                is UiState.Success -> {
                    val vets = state.data

                    if (vets.isEmpty()) {
                        // Empty state
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.SearchOff,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                                    modifier = Modifier.size(64.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "No vets found",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "Showing ${vets.size} Vets",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )

                        val listState = rememberLazyListState()

                        // Trigger loadNextPage when reaching last item
                        val shouldLoadMore by remember {
                            derivedStateOf {
                                val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                                val totalItems = listState.layoutInfo.totalItemsCount
                                lastVisible != null && lastVisible.index >= totalItems - 3
                            }
                        }
                        LaunchedEffect(shouldLoadMore) {
                            if (shouldLoadMore && !isLoadingMore) {
                                vetViewModel.loadMoreVets()
                            }
                        }


                        LazyColumn(
                            state = listState,
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                            modifier = Modifier.fillMaxSize(),

                        ) {
                            items(
                                items = vets,
                                key = { vet -> vet.id ?: UUID.randomUUID().toString() }
                            ) { vet ->
                                VetCard(
                                    vet = vet,
                                    onClick = { onVetClick(vet) },
                                    onBookAppointment = { onBookAppointment(vet) }
                                )
                            }

                            // Load more spinner at bottom
                            if (isLoadingMore) {
                                item {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    ) {
                                        CircularProgressIndicator(
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }
                                }
                            }

                            item { Spacer(modifier = Modifier.height(16.dp)) }
                        }
                    }
                }

                // Error state
                is UiState.Error -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.WifiOff,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = state.message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { vetViewModel.loadVetList() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }

                UiState.Idle -> TODO()
            }
        }
    }
}

// ─── Top Bar ──────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VetsTopBar(onBackClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "Vets List",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                // onSurface → Color.Black (light) / Color.White (dark)
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            // surface → PetSurface (light) / PetSurfaceDark (dark)
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

// ─── Search Bar ───────────────────────────────────────────────────────────────

@Composable
private fun VetSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {


    // Filter suggestions based on query
    val suggestions = remember(query) {
        if (query.length >= 2) {
            VET_SERVICES.filter {
                it.lowercase().startsWith(query.lowercase())
            }
        } else emptyList()
    }

    Column(modifier = modifier) {
        // ── Search Field ──────────────────────────────────────────────────────
        TextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = {
                Text(
                    text = "Search vet name or service...",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    fontSize = 13.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            },
            // Show clear button when typing
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor   = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor   = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor  = Color.Transparent,
                focusedTextColor        = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor      = MaterialTheme.colorScheme.onSurface,
                cursorColor             = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp)
                )
        )

        // ── Service Suggestions Chips ─────────────────────────────────────────
        // Appear when user types 2+ characters and matches a service
        if (suggestions.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 2.dp)
            ) {
                items(suggestions) { service ->
                    SuggestionChip(
                        onClick = { onQueryChange(service) },
                        label = {
                            Text(
                                text = service,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f )
                        ),
                        border = SuggestionChipDefaults.suggestionChipBorder(
                            enabled = true,
                            borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                        )
                    )
                }
            }
        }
    }
}

// ─── Filter / Sort / Map Row ──────────────────────────────────────────────────

@Composable
private fun FilterSortRow(
    modifier: Modifier = Modifier,
    activeFilterCount: Int = 0,
    onFilterClick: () -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        BadgedBox(
            badge = {
                if (activeFilterCount > 0) {
                    Badge(containerColor = MaterialTheme.colorScheme.primary) {
                        Text(
                            text     = activeFilterCount.toString(),
                            color    = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        ) {
            FilterChipItem(
                icon    = Icons.Default.FilterList,
                label   = "Filter",
                onClick = onFilterClick
            )
        }
        FilterChipItem(icon = Icons.Default.SwapVert, label = "Sort")
        FilterChipItem(icon = Icons.Default.Map,      label = "Map")
    }
}

@Composable
private fun FilterChipItem(icon: ImageVector, label: String,onClick: () -> Unit = {}) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = ButtonDefaults.outlinedButtonBorder(enabled = true),
        modifier = Modifier
            .height(36.dp)
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontSize = 13.sp
            )
        }
    }
}

// ─── Vet Card ─────────────────────────────────────────────────────────────────

@Composable
fun VetCard(
    vet: Vet,
    onClick: () -> Unit = {},
    onBookAppointment: () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(

            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            // ── Header ────────────────────────────────────────────────────────
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                NetworkCircleImage(
                    imageUrl = vet.imageUrl,
                    contentDescription = ""
                )

                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = vet.name ?: "Unknown Vet",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${vet.experienceYears ?: 0} years Exp.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    RatingRow(rating = vet.rating ?: 0.0)
                }
                Icon(
                    imageVector = Icons.Outlined.BookmarkBorder,
                    contentDescription = "Bookmark",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // ── Availability ──────────────────────────────────────────────────
            AvailabilityBadge(workingTime = vet.workingTime)

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.height(8.dp))

            // ── Location ──────────────────────────────────────────────────────
            InfoRow(
                icon = Icons.Default.LocationOn,
                text = vet.location ?: "Location not available",
                // primary → PetOrange / PetOrangeDark
                iconTint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(6.dp))

            // ── Price & Wait ──────────────────────────────────────────────────
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                InfoRow(
                    icon = Icons.Default.AttachMoney,
                    text = "Price: ${vet.price ?: 0} EGP",
                    iconTint = MaterialTheme.colorScheme.primary
                )
                InfoRow(
                    icon = Icons.Default.AccessTime,
                    text = "Waiting time: ${vet.waitingTimeMinutes ?: 0} mins",
                    iconTint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // ── Service Chips ─────────────────────────────────────────────────
            ServiceChipsRow(services = vet.services ?: emptyList())

            Spacer(modifier = Modifier.height(12.dp))

            // ── Book Button ───────────────────────────────────────────────────
            Button(
                onClick = onBookAppointment,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    // primary → PetOrange / PetOrangeDark
                    containerColor = MaterialTheme.colorScheme.primary,
                    // onPrimary → Color.White (both schemes)
                    contentColor   = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
            ) {
                Text(
                    text = "Book Appointment",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }
    }
}


// ─── Rating Row ───────────────────────────────────────────────────────────────

@Composable
private fun RatingRow(rating: Double) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Rating",
            tint = StarYellow,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = rating.toString(),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = "(ratings)",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
}

// ─── Availability Badge ───────────────────────────────────────────────────────

@Composable
private fun AvailabilityBadge(workingTime: String?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(AvailableGreen)  // fixed semantic color
        )
        Text(
            text = "Available Today ${workingTime ?: ""}",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = AvailableGreen           // fixed semantic color
        )
    }
}

// ─── Info Row ─────────────────────────────────────────────────────────────────

@Composable
private fun InfoRow(
    icon: ImageVector,
    text: String,
    iconTint: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = text,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ─── Service Chips ────────────────────────────────────────────────────────────

@Composable
private fun ServiceChipsRow(services: List<String>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        services.take(4).forEach { service ->
            ServiceChip(label = service)
        }
    }
}

@Composable
private fun ServiceChip(label: String) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        // secondaryContainer → tinted chip from PetGreen / PetGreenDark palette
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f ),
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

//// ─── Previews ─────────────────────────────────────────────────────────────────
//
//@androidx.compose.ui.tooling.preview.Preview(showBackground = true, name = "Light")
//@Composable
//private fun VetsListLightPreview() {
//    PetMateTheme(darkTheme = false) { VetsListScreen() }
//}
//
//@androidx.compose.ui.tooling.preview.Preview(
//    showBackground = true,
//    name = "Dark",
//    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
//)
//@Composable
//private fun VetsListDarkPreview() {
//    PetMateTheme(darkTheme = true) { VetsListScreen() }
//}