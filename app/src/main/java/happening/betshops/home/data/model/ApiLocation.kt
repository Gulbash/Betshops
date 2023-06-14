package happening.betshops.home.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiLocation(
    @SerialName("lat")
    val lat: Double,
    @SerialName("lng")
    val lng: Double
)