package uk.ac.wlv.petmate.data.model

data class VetFilterState(
    val selectedServices : List<String> = emptyList(),
    val minRating        : Float        = 0f,
    val maxPrice         : Int          = 5000,
    val maxWaitingTime   : Int          = 60,
    val sortBy           : SortOption   = SortOption.NONE
)