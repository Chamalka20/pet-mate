package uk.ac.wlv.petmate.data.repository

import uk.ac.wlv.petmate.data.model.Vet
import uk.ac.wlv.petmate.data.model.VetFilterState

interface VetRepository {
    val isLastPage: Boolean
    suspend fun getVetList(isRefresh   : Boolean        = false,
                           filter      : VetFilterState = VetFilterState(),
                           searchQuery : String         = ""): List<Vet>

    suspend fun getVet(vetId: Int): Vet
}

