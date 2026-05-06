package uk.ac.wlv.petmate.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import uk.ac.wlv.petmate.data.model.BottomNavItem

@Composable
fun PetMateBottomBar(navController: NavHostController) {

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.MedLog,
        BottomNavItem.Emergency,
        BottomNavItem.Mating,
        BottomNavItem.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val emergencyItem = items[2]

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {

        NavigationBar(
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color.White,

        ) {
            items.take(2).forEach { item ->
                BottomBarItem(item, navController, currentRoute)
            }

            NavigationBarItem(
                selected = false,
                onClick  = {},
                icon     = { Spacer(Modifier.size(24.dp)) },
                enabled  = false,
                colors   = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )

            items.takeLast(2).forEach { item ->
                BottomBarItem(item, navController, currentRoute)
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-19).dp)
                .clickable {
                    navController.navigate("emergency") {
                        popUpTo("home")
                        launchSingleTop = true
                    }
                }
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        color = if (currentRoute != "emergency")
                            MaterialTheme.colorScheme.surface
                        else
                            MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                    .border(
                        width = 4.dp,
                        color = Color.White,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    painter            = painterResource(id = emergencyItem.icon),
                    contentDescription = "Emergency",
                    tint               = if (currentRoute != "emergency") Color.Gray else Color.White,
                    modifier           = Modifier.size(28.dp)
                )
            }

            Text(
                text      = "Emergency",
                fontSize  = 10.sp,
                fontWeight = FontWeight.W500,
                color     = if (currentRoute != "emergency") Color.Gray
                else MaterialTheme.colorScheme.primary,
            )
        }
    }
}


@Composable
fun RowScope.BottomBarItem(
    item         : BottomNavItem,
    navController: NavHostController,
    currentRoute : String?
) {
    NavigationBarItem(
        selected = currentRoute == item.route,
        onClick  = {
            navController.navigate(item.route) {
                popUpTo("home")
                launchSingleTop = true
            }
        },
        icon = {
            Icon(
                painter            = painterResource(id = item.icon),
                contentDescription = item.title,
                modifier = Modifier.size(24.dp)
            )
        },
        label  = { Text(item.title, fontSize = 10.sp) },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor   = MaterialTheme.colorScheme.primary,
            selectedTextColor   = MaterialTheme.colorScheme.primary,
            unselectedIconColor = Color.Gray,
            unselectedTextColor = Color.Gray,
            indicatorColor      = Color.Transparent
        )
    )
}