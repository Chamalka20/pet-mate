package uk.ac.wlv.petmate.data.repository

import uk.ac.wlv.petmate.data.model.NominatimResult

interface LocationSearchRepository {
    suspend fun searchLocation(query: String): List<NominatimResult>
}
