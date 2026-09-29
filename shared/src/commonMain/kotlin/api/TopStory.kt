package api

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull


@Serializable(with=ItemResponseSerializer::class)
sealed interface ItemResponse {

    @Serializable
    @SerialName("story")
    data class Story(
        val id: Long,
        val type: String,
        val time: Long,
        val by: String? = null,
        val title: String?  = null,
        val score: Int? = null,
        val url: String? = null,
        val descendants: Int? = null,
        val kids: List<Long>? = null,
        val text: String? = null
    ) : ItemResponse

    @Serializable
    data object  NullResponse : ItemResponse

}

internal object ItemResponseSerializer :
        JsonContentPolymorphicSerializer<ItemResponse>(ItemResponse::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<ItemResponse> = when {

        element is  JsonNull -> ItemResponse.NullResponse.serializer()
        else -> ItemResponse.Story.serializer()

    }
}