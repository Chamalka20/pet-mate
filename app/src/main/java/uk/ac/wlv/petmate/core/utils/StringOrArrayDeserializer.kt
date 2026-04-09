package uk.ac.wlv.petmate.core.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class StringOrArrayDeserializer : JsonDeserializer<List<String>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): List<String> {
        if (json == null || json.isJsonNull) {
            return emptyList()
        }

        return when {
            json.isJsonArray -> json.asJsonArray.map { it.asString }
            json.isJsonPrimitive -> listOf(json.asString)
            else -> emptyList()
        }
    }
}