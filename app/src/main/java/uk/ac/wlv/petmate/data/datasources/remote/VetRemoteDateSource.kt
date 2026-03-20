package uk.ac.wlv.petmate.data.datasources.remote

import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import uk.ac.wlv.petmate.data.model.Pet
import uk.ac.wlv.petmate.data.model.Vet

class VetRemoteDateSource {

    private val firestore = Firebase.firestore
    private fun vetsCollection() = firestore.collection("vets")

    companion object {
        private const val PAGE_SIZE = 10L
    }

    private var lastVisibleDocument: DocumentSnapshot? = null
    var isLastPage = false
        private set                  // ViewModel can read it, only this class can write it

    suspend fun getVetsList( isRefresh: Boolean = false): List<Vet> {
        if (isRefresh) {
            lastVisibleDocument = null
            isLastPage = false
        }

        if (isLastPage) return emptyList()

        val query = vetsCollection().limit(PAGE_SIZE)

        val snapshot = (if (lastVisibleDocument != null)
            query.startAfter(lastVisibleDocument!!)
        else
            query).get().await()

        lastVisibleDocument = snapshot.documents.lastOrNull()
        isLastPage = snapshot.documents.size < PAGE_SIZE

        return snapshot.documents.mapNotNull { it.toObject(Vet::class.java) }
    }

    suspend fun getVet( vetId: String): Vet {
        val snapshot = vetsCollection().document(vetId).get().await()
        return snapshot.toObject(Vet::class.java)
            ?: throw Exception("Pet not found")
    }

}