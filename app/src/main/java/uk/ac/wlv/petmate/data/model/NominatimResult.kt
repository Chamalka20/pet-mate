package uk.ac.wlv.petmate.data.model

data class NominatimResult(
    val place_id   : Long,
    val display_name: String,
    val lat        : String,
    val lon        : String
)