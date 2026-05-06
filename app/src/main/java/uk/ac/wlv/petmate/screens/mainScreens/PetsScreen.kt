package uk.ac.wlv.petmate.screens.mainScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun PetsScreen() {

    Scaffold(
        containerColor = Color(0xFF455A64) // background color
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "Welcome to Pets Screen 🐾",
                fontSize = 22.sp,
                color = Color.White
            )
        }
    }
}