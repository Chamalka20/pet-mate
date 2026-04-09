package uk.ac.wlv.petmate.data.repository

import uk.ac.wlv.petmate.data.model.Pet

interface PetRepository {

    suspend fun savePet(pet: Pet):Boolean

    suspend fun getPetList(): List<Pet>

   suspend fun getPet(petId: Int): Pet

   suspend fun updatePet(petId: Int, pet: Pet): Boolean

    suspend fun deletePet(petId: Int) : Boolean
}
