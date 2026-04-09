package uk.ac.wlv.petmate.data.model

// { total, page, pageSize, data }
data class VetsResponse(
    val total: Int,
    val page: Int,
    val pageSize: Int,
    val data: List<Vet>
)