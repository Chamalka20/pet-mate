package uk.ac.wlv.petmate.data.network

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.Response
import retrofit2.http.Path
import uk.ac.wlv.petmate.data.model.Pet
import uk.ac.wlv.petmate.data.model.PetListResponse

interface PetApiService {

    @POST("api/pets/create")
    suspend fun createPet(
        @Header("Authorization") token: String,
        @Body request: Pet
    ): Response<ResponseBody>

    @GET("api/pets/list")
    suspend fun getPetList(
        @Header("Authorization") token: String
    ): Response<PetListResponse>

    @GET("api/pets/{petId}")
    suspend fun getPet(
        @Header("Authorization") token: String,
        @Path("petId") petId: Int
    ): Response<Pet>

    @PUT("api/pets/{petId}")
    suspend fun updatePet(
        @Header("Authorization") token: String,
        @Path("petId") petId: Int,
        @Body request: Pet
    ): Response<ResponseBody>

    @DELETE("api/pets/{petId}")
    suspend fun deletePet(
        @Header("Authorization") token: String,
        @Path("petId") petId: Int
    ): Response<ResponseBody>
}