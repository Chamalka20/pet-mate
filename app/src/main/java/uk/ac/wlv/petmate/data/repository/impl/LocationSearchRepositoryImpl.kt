package uk.ac.wlv.petmate.data.repository.impl

import uk.ac.wlv.petmate.data.model.NominatimResult
import uk.ac.wlv.petmate.data.network.NominatimService
import uk.ac.wlv.petmate.data.repository.LocationSearchRepository

class LocationSearchRepositoryImpl(
    private val nominatimService: NominatimService
) : LocationSearchRepository {

    override suspend fun searchLocation(query: String): List<NominatimResult> {
        return try {
            nominatimService.searchLocation(query)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
