package uk.ac.wlv.petmate.screens.vet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import uk.ac.wlv.petmate.components.NetworkCircleImage
import uk.ac.wlv.petmate.core.UiState
import uk.ac.wlv.petmate.data.model.Vet
import uk.ac.wlv.petmate.screens.vet.Components.MapPreview
import uk.ac.wlv.petmate.screens.vet.Components.VetDetailsShimmer
import uk.ac.wlv.petmate.viewmodel.VetViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VetDetailsScreen(vetId:Int,
                     vetViewModel: VetViewModel,
                     navController: NavHostController,) {

    val vetState by vetViewModel.selectedVetState.collectAsState()

    LaunchedEffect(vetId) {
        vetViewModel.loadVet(vetId)
    }

    Scaffold(containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Vet Details") },
                navigationIcon = {
                    IconButton(onClick = {   navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },

            )
        }) { padding ->
        when (val state = vetState) {
            is UiState.Loading -> Column( modifier = Modifier
                .fillMaxSize()
                .padding(padding)) {
                VetDetailsShimmer()
            }
            is UiState.Success -> {
                val vet = state.data
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {

                    VetHeader(vet)

                    Spacer(modifier = Modifier.height(16.dp))

                    ServicesSection(vet.services)

                    Spacer(modifier = Modifier.height(16.dp))

                    VetInfoItem(
                        icon = Icons.Default.AttachMoney,
                        text = "Price: ${vet.price} EGP"
                    )

                    VetInfoItem(
                        icon = Icons.Default.AccessTime,
                        text = "${vet.workingDays}  ${vet.workingTime}"
                    )

                    VetInfoItem(
                        icon = Icons.Default.LocationOn,
                        text = vet.location
                    )

                    VetInfoItem(
                        icon = Icons.Default.Star,
                        text = "You'll get ${vet.rewardPoints} Paw Points with this booking"
                    )

                    VetInfoItem(
                        icon = Icons.Default.Timer,
                        text = "Waiting time: ${vet.waitingTimeMinutes} mins"
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Clinic Details",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    MapPreview(latitude =vet.latitude, longitude = vet.longitude )
                }
            }
            is UiState.Error -> { Text(
                text = "",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ) }
            else -> Unit
        }

    }
}

@Composable
fun VetHeader(vet: Vet) {

    Row(verticalAlignment = Alignment.CenterVertically) {


        NetworkCircleImage(
            imageUrl = vet.imageUrl,
            contentDescription = ""
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {

            vet.name?.let {
                Text(
                    text = it,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            vet.specialization?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Text(
                text = "${vet.experienceYears} years Exp.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )

            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(16.dp)
                )

                Text(" ${vet.rating}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black)
            }
        }
    }
}

@Composable
fun ServicesSection(services: List<String>?) {

    Column {

        Text(
            text = "Services",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {

            services?.forEach {

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f ),
                    modifier = Modifier.padding(end = 8.dp)
                ) {

                    Text(
                        text = it,
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun VetInfoItem(
    icon: ImageVector,
    text: String?
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {

        Icon(
            icon,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(17.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        if (text != null) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )

        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun VetScreenPreview() {
//
//    val vet = Vet(
//        id = 1,
//        name = "Dr. Mostafa Khalid",
//        specialization = "General Veterinarian (VMD)",
//        experienceYears = 20,
//        rating = 5.0,
//        services = listOf("Grooming", "Therapy", "X-rays", "In-Home Visit"),
//        price = 120,
//        workingDays = "Sat - Thurs",
//        workingTime = "6:00 PM - 12:00 PM",
//        location = "Al Gondy Al Maqbool Sq., Al Mansheyah",
//        rewardPoints = 200,
//        waitingTimeMinutes = 10,
//        imageUrl = ""
//    )
//
//    VetDetailsScreen(vet)
//}