package uk.ac.wlv.petmate.data.repository.impl

import uk.ac.wlv.petmate.data.datasources.remote.VetRemoteDateSource
import uk.ac.wlv.petmate.data.model.Pet
import uk.ac.wlv.petmate.data.model.Vet
import uk.ac.wlv.petmate.data.repository.VetRepository

class VetRepositoryImpl( private val remoteDataSource: VetRemoteDateSource): VetRepository {
    override val isLastPage: Boolean
        get() = remoteDataSource.isLastPage
    override suspend fun getVetList(isRefresh: Boolean): List<Vet> {
        return remoteDataSource.getVetsList(isRefresh)
    }
    override suspend fun getVet(petId: String): Vet {
        return remoteDataSource.getVet( petId)
    }

}