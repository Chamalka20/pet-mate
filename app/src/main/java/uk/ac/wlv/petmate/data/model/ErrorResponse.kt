package uk.ac.wlv.petmate.data.model

data class ErrorResponse(
    val message: String,
    val errors: List<String>?
)