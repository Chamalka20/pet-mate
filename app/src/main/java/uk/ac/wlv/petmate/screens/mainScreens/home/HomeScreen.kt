package uk.ac.wlv.petmate.screens.mainScreens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import uk.ac.wlv.petmate.R
import uk.ac.wlv.petmate.components.shimmers.PetShimmerRow
import uk.ac.wlv.petmate.core.UiState
import uk.ac.wlv.petmate.data.model.Pet
import uk.ac.wlv.petmate.data.model.Reminder
import uk.ac.wlv.petmate.data.model.ServiceItem
import uk.ac.wlv.petmate.data.model.Vet
import uk.ac.wlv.petmate.screens.mainScreens.home.Components.AddPetItem
import uk.ac.wlv.petmate.screens.mainScreens.home.Components.EmergencyCard
import uk.ac.wlv.petmate.screens.mainScreens.home.Components.PetItem
import uk.ac.wlv.petmate.screens.mainScreens.home.Components.ReminderCard
import uk.ac.wlv.petmate.screens.mainScreens.home.Components.ServiceItemCard
import uk.ac.wlv.petmate.screens.mainScreens.home.Components.VetCard
import uk.ac.wlv.petmate.ui.theme.DisplayFontFamily
import uk.ac.wlv.petmate.viewmodel.PetProfileViewModel
import uk.ac.wlv.petmate.viewmodel.SessionViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(sessionViewModel: SessionViewModel = koinViewModel(),petProfileViewModel: PetProfileViewModel,rootNavController:NavController,) {
    val user by sessionViewModel.currentUser
    val petListState by petProfileViewModel.petListState.collectAsStateWithLifecycle()
    val services = listOf(
        ServiceItem("Clinic Visit", R.drawable.clinic_visit),
        ServiceItem("Home Visit", R.drawable.home_visit),
        ServiceItem("Mating", R.drawable.mating),
        ServiceItem("Emergency", R.drawable.emergency),
        ServiceItem("Vaccination", R.drawable.vaccination),
        ServiceItem("Grooming", R.drawable.grooming)
    )
    fun getSampleReminders() = listOf(
        Reminder(
            title = "Morning exercise",
            subtitle = "Walk with Oscar",
            time = "10:00 AM - 10:30 AM"
        ),
        Reminder(
            title = "Medication",
            subtitle = "Fever Check",
            time = "10:00 AM"
        )
    )

    fun getSampleVets(): List<Vet> {
        return listOf(
            Vet("Dr. Mostafa Khalid", 5.0, "Mansheya, Alexandria", "120 EGP", "https://res.cloudinary.com/dclwrplu9/image/upload/v1772711631/Gemini_Generated_Image_pd2ce9pd2ce9pd2c_e7z1rz.png"),
            Vet("Dr. Ghada Younes", 5.0, "Bab Sharqi", "100 EGP", "https://res.cloudinary.com/dclwrplu9/image/upload/v1772711631/Gemini_Generated_Image_pd2ce9pd2ce9pd2c_e7z1rz.png"),
            Vet("Dr. Ahmed Ali", 4.8, "Miami, Alexandria", "110 EGP", "https://res.cloudinary.com/dclwrplu9/image/upload/v1772711631/Gemini_Generated_Image_pd2ce9pd2ce9pd2c_e7z1rz.png")
        )
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(color = Color(0xFFFF9800,), fontFamily = DisplayFontFamily,)
                            ) {
                                append("Pet")
                            }
                            withStyle(
                                style = SpanStyle(color = Color.Black, fontFamily = DisplayFontFamily,)
                            ) {
                                append("Mate")
                            }
                        },
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = {
                        // TODO: notification click action
                    }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications"
                        )
                    }
                }
            )
        }
    ) {padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.TopStart

        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)

            ) {
                item {
                    Text(
                        "Hello,${user?.name}",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                item {
                    when (petListState) {

                        is UiState.Idle -> Unit

                        is UiState.Loading -> {
                            PetShimmerRow(
                                itemCount = 4,

                            )
                        }

                        is UiState.Success -> {
                            val pets = (petListState as UiState.Success<List<Pet>>).data

                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp, ),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(pets) { pet ->
                                    PetItem(
                                        pet,
                                        onClick = {
                                            rootNavController.navigate(
                                                "petDetailsScreen/${pet.id}"
                                            )
                                        }
                                    )
                                }

                                item {
                                    AddPetItem(
                                        onClick = {
                                            rootNavController.navigate(
                                                "petProfileSetup?isDefaultAddBackButton=true"
                                            )
                                        }
                                    )
                                }
                            }
                        }

                        is UiState.Error -> {
                            val message = (petListState as UiState.Error).message
                            Text(
                                text = message,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }

                item {
                    Text(
                        "Quick Services",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                item {
                    Box(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
                        LazyVerticalGrid(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp), 
                            userScrollEnabled = false,
                            columns = GridCells.Fixed(3),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)

                        ) {
                            items(services) { item ->
                                ServiceItemCard(item)
                            }
                        }
                    }
                }
                item {
                Box(modifier = Modifier.padding(top = 16.dp, bottom  = 16.dp)) {
                    EmergencyCard(onClick = {})
                    }
                }
                item {
                    Row(  modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween) {

                        Text(
                            "Activity Reminder",
                            style = MaterialTheme.typography.titleMedium,

                        )

                        Row{

                            Text(
                                "View All",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray ),

                                )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = null,
                                tint =Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                    }

                }
                item {

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        items(getSampleReminders()) { reminder ->
                            ReminderCard(reminder)
                        }
                    }
                }

                item{
                    Image(

                        painter = painterResource(id = R.drawable.ad_banner),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(horizontal = 16.dp,)
                    )
                }

                item {
                    Row(  modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween) {

                        Text(
                            "Nearby Vets",
                            style = MaterialTheme.typography.titleMedium,

                            )

                        Row{

                            Text(
                                "View All",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray ),

                                )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = null,
                                tint =Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                    }

                }

                item{
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        items(getSampleVets()) { vet ->
                            VetCard(vet)
                        }
                    }
                }


            }

        }
    }



}







