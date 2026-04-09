package uk.ac.wlv.petmate.data.model

import com.google.gson.annotations.JsonAdapter
import uk.ac.wlv.petmate.core.utils.StringOrArrayDeserializer

data class Pet(
    val id: Int = 0,
    val name: String = "",
    var type: PetType? = null,
    val breed: String = "",
    val age: Int = 0,
    val weight: Double = 0.0,
    val gender: Gender = Gender.MALE,
    val isSpayedNeutered: Boolean = false,
    val medicalConditions: List<String> = emptyList(),
    val allergies: List<String> = emptyList(),

    val imageUrl: String = ""
)

enum class PetType {
    CAT, DOG,BIRD,RABBIT,HAMSTER,FISH,REPTILE
}

enum class Gender {
    MALE, FEMALE, UNKNOWN
}

data class MedicalCondition(
    val id: String,
    val name: String,
    val isSelected: Boolean = false
)

data class Allergy(
    val id: String,
    val name: String,
    val isSelected: Boolean = false
)