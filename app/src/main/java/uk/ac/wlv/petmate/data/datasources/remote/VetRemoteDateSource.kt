package uk.ac.wlv.petmate.data.datasources.remote

import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import uk.ac.wlv.petmate.data.model.Pet
import uk.ac.wlv.petmate.data.model.Vet
import uk.ac.wlv.petmate.data.network.ApiClient
import kotlin.math.ceil

class VetRemoteDataSource(

) {

    companion object {
        private const val PAGE_SIZE = 10
    }

    private var currentPage = 1
    var isLastPage = false
        private set

    suspend fun getVetsList(isRefresh: Boolean = false): List<Vet> {
        if (isRefresh) {
            currentPage = 1
            isLastPage = false
        }

        if (isLastPage) return emptyList()

        val response =ApiClient.vetApi.getVets(page = currentPage, pageSize = PAGE_SIZE)

        val totalPages = ceil(response.total.toDouble() / PAGE_SIZE).toInt()
        isLastPage = currentPage >= totalPages

        if (!isLastPage) currentPage++

        return response.data
    }

    suspend fun getVet(vetId: Int): Vet {
        return ApiClient.vetApi.getVet(id =  vetId)
            ?: throw Exception("Vet not found")
    }
}