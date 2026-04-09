package uk.ac.wlv.petmate.data.model

data class RegisterRequest(
    val fullName: String,
    val email: String,
    val password: String,
    val phoneNumber: String? = null,
    val profilePhotoUrl: String? = null
)