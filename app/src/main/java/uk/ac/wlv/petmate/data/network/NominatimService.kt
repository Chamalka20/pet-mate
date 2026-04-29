package uk.ac.wlv.petmate.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import uk.ac.wlv.petmate.data.model.NominatimResult

interface NominatimService {

    @GET("search")
    suspend fun searchLocation(
        @Query("q")       query  : String,
        @Query("format")  format : String = "json",
        @Query("limit")   limit  : Int    = 5,
        @Query("addressdetails") addressDetails: Int = 1
    ): List<NominatimResult>
}