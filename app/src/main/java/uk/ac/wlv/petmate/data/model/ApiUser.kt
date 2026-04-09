package uk.ac.wlv.petmate.data.model

data class ApiUser(
    val id: String,
    val fullName: String?,
    val email: String?,
    val phoneNumber: String?,
    val profilePhotoUrl: String?,
    val isGoogleUser: Boolean,
    val token: String,
    val createdAt: String
)