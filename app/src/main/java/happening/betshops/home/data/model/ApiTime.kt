package happening.betshops.home.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ApiTime(
    @SerialName("dstOffset")
    val dstOffset: Int,
    @SerialName("rawOffset")
    val rawOffset: Int,
    @SerialName("status")
    val status: String,
    @SerialName("timeZoneId")
    val timeZoneId: String,
    @SerialName("timeZoneName")
    val timeZoneName: String
)
