package happening.betshops.home.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiBetshop(
    @SerialName("name")
    val name: String,
    @SerialName("location")
    val location: ApiLocation,
    @SerialName("id")
    val id: Int,
    @SerialName("county")
    val county: String,
    @SerialName("city_id")
    val cityId: Int,
    @SerialName("city")
    val city: String,
    @SerialName("address")
    val address: String,
)
