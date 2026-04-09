package uk.ac.wlv.petmate.data.model

data class AuthResponse(
    val message: String,
    val user: ApiUser
)