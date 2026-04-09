package uk.ac.wlv.petmate.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import uk.ac.wlv.petmate.data.model.Vet
import uk.ac.wlv.petmate.data.model.VetsResponse

interface VetApiService {

    @GET("api/vets/list")
    suspend fun getVets(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): VetsResponse

    @GET("api/vets/{id}")
    suspend fun getVet(
        @Path("id") id: Int
    ): Vet
}