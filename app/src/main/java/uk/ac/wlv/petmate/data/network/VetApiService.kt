package uk.ac.wlv.petmate.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import uk.ac.wlv.petmate.data.model.Vet
import uk.ac.wlv.petmate.data.model.VetsResponse

interface VetApiService {

    @GET("api/vets/list")
    suspend fun getVets(
        @Query("page")           page         : Int,
        @Query("pageSize")       pageSize     : Int,
        @Query("searchQuery")    searchQuery  : String? = null,
        @Query("services")       services     : List<String>? = null,
        @Query("minRating")      minRating    : Float? = null,
        @Query("maxPrice")       maxPrice     : Int? = null,
        @Query("maxWaitingTime") maxWaitingTime: Int? = null,
        @Query("sortBy")         sortBy       : String? = null
    ): VetsResponse

    @GET("api/vets/{id}")
    suspend fun getVet(
        @Path("id") id: Int
    ): Vet
}