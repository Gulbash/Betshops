package happening.betshops.home.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiBetshopResults(
    @SerialName("count")
    val count: Int,
    @SerialName("betshops")
    val betshops: List<ApiBetshop>
)
