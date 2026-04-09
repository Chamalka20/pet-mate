package uk.ac.wlv.petmate.data.repository

import uk.ac.wlv.petmate.data.model.Vet

interface VetRepository {
    val isLastPage: Boolean
    suspend fun getVetList(isRefresh: Boolean =false): List<Vet>

    suspend fun getVet(vetId: Int): Vet
}

