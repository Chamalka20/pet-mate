package uk.ac.wlv.petmate.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import uk.ac.wlv.petmate.components.PetMateBottomBar
import uk.ac.wlv.petmate.screens.mainScreens.CareScreen
import uk.ac.wlv.petmate.screens.mainScreens.home.HomeScreen
import uk.ac.wlv.petmate.screens.mainScreens.PetsScreen
import uk.ac.wlv.petmate.screens.mainScreens.ProfileScreen
import androidx.navigation.compose.rememberNavController
import uk.ac.wlv.petmate.screens.mainScreens.EmergencyScreen
import uk.ac.wlv.petmate.viewmodel.PetProfileViewModel
import uk.ac.wlv.petmate.viewmodel.VetViewModel

@Composable
fun MainScreen(rootNavController: NavController,petProfileViewModel: PetProfileViewModel,vetViewModel: VetViewModel) {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = { PetMateBottomBar(bottomNavController) }

    ) { paddingValues ->

        NavHost(
            navController = bottomNavController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") { HomeScreen(rootNavController = rootNavController, petProfileViewModel = petProfileViewModel, vetViewModel =vetViewModel ) }
            composable("medlog") { PetsScreen() }
            composable("emergency") { EmergencyScreen() }
            composable("mating") { CareScreen() }
            composable("profile") { ProfileScreen() }
        }
    }
}

