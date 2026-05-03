package uk.ac.wlv.petmate.data.model

import uk.ac.wlv.petmate.R


sealed class BottomNavItem(
    val route : String,
    val title : String,
    val icon  : Int
) {
    object Home      : BottomNavItem("home",      "Home",      R.drawable.ic_home)
    object MedLog    : BottomNavItem("medlog",    "MedLog",    R.drawable.ic_medlog)
    object Emergency : BottomNavItem("emergency", "Emergency", R.drawable.ic_emergency)
    object Mating    : BottomNavItem("mating",    "Mating",    R.drawable.ic_mating)
    object Profile   : BottomNavItem("profile",   "Profile",   R.drawable.ic_profile)
}