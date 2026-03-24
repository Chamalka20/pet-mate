package uk.ac.wlv.petmate.data.model

data class Vet(
    val id: String? = null,
    val name: String? = null,
    val specialization: String? = null,
    val experienceYears: Int? = null,
    val rating: Double? = null,
    val services: List<String>? = null,
    val price: Int? = null,
    val workingDays: String? = null,
    val workingTime: String? = null,
    val location: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val rewardPoints: Int? = null,
    val waitingTimeMinutes: Int? = null,
    val imageUrl: String? = null
)